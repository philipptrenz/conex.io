package mapping.get.functionMapper;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.swagger.RFC3339DateFormat;
import mapping.MappingHelper;
import mapping.exceptions.NoValidKeyPathException;

/**
 * The Class MappingValueExtractor.
 * 
 * This class validates the incoming FHEM device from jsonlist2 as JSON to fit the requirements
 * to get mapped defined in the FHEM module description (json file).
 */
public class ValueExtractor {
	
	/**
	 * Extract value.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @param property the property
	 * @return the string
	 */
	
	
	public String extractValue(JsonNode jsonlist2Device, JsonNode property, String propertyName, Object function) {		
		
		String key_path = property.get("key_path").asText();
		String unmappedDeviceValue;
		try {
			unmappedDeviceValue = MappingHelper.navigateJsonKeyPath(jsonlist2Device, key_path).asText();
			return extractValue(unmappedDeviceValue, property, propertyName, function);
		} catch (NoValidKeyPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String extractValue(String unmappedDeviceValue, JsonNode property, String propertyName, Object function) {
		
		// watch for property name
		switch(propertyName) {
		
		case "timestamp": return getTimestamp(unmappedDeviceValue, property);
		
		default: break;
		}
		
		if (!property.has("cases")) System.out.println("this shouldn't happen");
		ArrayNode cases = (ArrayNode) property.get("cases");

		String value = "";
		for (JsonNode mappingCase : cases) {
			String extractMode = mappingCase.get("extract_mode").asText();
			
			// watch for extract mode
			switch (extractMode) {
			
			case "direct": 
				value = modeDirect(mappingCase, unmappedDeviceValue, propertyName, function);
				if (value != null && !value.isEmpty()) {
					return value;
				}
			
			case "range":
				value = modeRange(mappingCase, unmappedDeviceValue, propertyName, function);
				if (value != null && !value.isEmpty()) {
					return value;
				}
				
			default:
				//System.out.println("extract_mode '"+extractMode+"' not found for value '"+unmappedDeviceValue+"', maybe function description is made for newer version of conex.io core");
				break;
			}
			
		}
		
		/*
		 * TODO: 
		 * 1. look which extract_mode is given and to which type will be mapped
		 * 2. choose correct extraction method
		 * 3. extract and convert value to Java (for validation)
		 * 4. convert to string and return
		 * 
		 */
		
		String defaultValue = property.get("default").asText();
		return defaultValue;
	}
	
	private String getTimestamp(String unmappedDeviceValue, JsonNode property) {
		try {
			String pattern = property.get("format").asText();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			RFC3339DateFormat format = new RFC3339DateFormat();
			
			return format.format(simpleDateFormat.parse(unmappedDeviceValue));
			//return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}		
	}
	
	private String modeDirect(JsonNode mappingCase, String unmappedDeviceValue, String propertyName, Object function) {
		ArrayNode regexList = (ArrayNode) mappingCase.get("regex");
		for (JsonNode regex : regexList ) {
			if (unmappedDeviceValue.matches(regex.asText())) {
				
				if (mappingCase.has("value")) {
					return mappingCase.get("value").asText();
					
				} else if (mappingCase.has("constraint")) {
					
					String type = mappingCase.get("constraint").asText();
					int constValue = getConstraintValueFromFunctionClassAnnotation(type, propertyName, function);
					
					return Integer.toString(constValue);	
					
				} else {
					System.out.println("None of the available mappings for direct mode are fitting");
				}
			}
		}
		return null;
	}
	
	private String modeRange(JsonNode mappingCase, String unmappedDeviceValue, String propertyName, Object function) {
		
		try {
			double minimumSourceValue = mappingCase.get("minimum").asDouble();
			double maximumSourceValue = mappingCase.get("maximum").asDouble();
			int minimumDestinationValue = getConstraintValueFromFunctionClassAnnotation("min", propertyName, function);
			int maximumDestinationValue = getConstraintValueFromFunctionClassAnnotation("max", propertyName, function);
			
			String regex = mappingCase.get("regex").asText();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(unmappedDeviceValue);

			if(!matcher.find()) return null;
			
			String sourceStringValue = matcher.group(1);
			sourceStringValue.replace(",", ".");
			double sourceValue = Double.parseDouble(sourceStringValue);
		    
		    // normalize source value as rational number
		    double rationalSourceValue = (sourceValue-minimumSourceValue) / (maximumSourceValue-minimumDestinationValue);
		    double destinationValue = rationalSourceValue * (maximumDestinationValue-minimumDestinationValue)+minimumDestinationValue;
		    
		    int value = (int) destinationValue;
		    return String.valueOf(value);

		} catch (Exception e) {
			return null;
		}
		
	}
	
	private int getConstraintValueFromFunctionClassAnnotation(String type, String propertyName, Object function) {
		try {
			Method[] methods = function.getClass().getMethods();
			Method setMethod = null;
			for (Method m : methods) {
				
				if (propertyName == null) System.out.println("ups");
				
				if (m.getName().toLowerCase().contains("get"+propertyName.toLowerCase())) {
					setMethod = m;
					
					if (type.equals("min")) {
						return (int) setMethod.getAnnotation(Min.class).value();
					} else if (type.equals("max")) {
						return (int) setMethod.getAnnotation(Max.class).value();
					} else {
						System.out.println("Function 'getConstraintValueFromFunctionClassAnnotation' has no case for type '"+type+"'");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

}
