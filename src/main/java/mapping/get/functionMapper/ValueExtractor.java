package mapping.get.functionMapper;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.util.NameTransformer;

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
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String log_info = "";
	
	/**
	 * Extract value.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @param property the property
	 * @return the string
	 */
	
	public String extractValue(JsonNode jsonlist2Device, JsonNode property, String propertyName, Object function, String log_info) {		
		
		String key_path = property.get("key_path").asText();
		String unmappedDeviceValue;
		try {
			unmappedDeviceValue = MappingHelper.navigateJsonKeyPath(jsonlist2Device, key_path).asText();
			
			if (unmappedDeviceValue == null) log.warn("unmappedDeviceValue is null! "+this.log_info);
			if (property == null) log.warn("property is null! "+this.log_info);
			if (propertyName == null) log.warn("propertyName is null! "+this.log_info);
			if (function == null) log.warn("function is null! "+this.log_info);
			
			return extractValue(unmappedDeviceValue, property, propertyName, function, log_info);
		} catch (NoValidKeyPathException e) {
			log.error("NoValidKeyPathException! "+log_info+", propertyName: "+propertyName+", function: "+function.getClass().getSimpleName());
			return null;
		}
	}
	
	
	/*
	 * BE AWARE: If string is null it will be rejected. To set property value null set string = "null"!
	 */
	public String extractValue(String unmappedDeviceValue, JsonNode property, String propertyName, Object function, String log_info) {
		
		this.log_info = log_info+", propertyName: "+propertyName+", function: "+function.getClass().getSimpleName();
		
		// watch for property name
		switch(propertyName) {
		
		case "timestamp": return getTimestamp(unmappedDeviceValue, property);
		
		default: break;
		}
		
		ArrayNode cases = (ArrayNode) property.get("cases");

		String value = "";
		for (JsonNode mappingCase : cases) {
			String extractMode = mappingCase.get("extract_mode").asText();
			
			// watch for extract mode
			switch (extractMode) {
			
			case "direct": 
				value = modeDirect(mappingCase, unmappedDeviceValue, propertyName, function);
				break;
			
			case "range":
				value = modeRange(mappingCase, unmappedDeviceValue, propertyName, function);
				break;
			case "constraint":
				value = modeConstraint(mappingCase, unmappedDeviceValue, propertyName, function);
				break;
			}
			if (value == null) log.warn("Value is null! "+this.log_info);
			return value;
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
		} catch (Exception e) {
			log.error("Date mapping failed", e);
			return "";
		}		
	}
	
	private String modeDirect(JsonNode mappingCase, String unmappedDeviceValue, String propertyName, Object function) {
		ArrayNode regexList = (ArrayNode) mappingCase.get("regex");
		for (JsonNode regex : regexList ) {
			if (unmappedDeviceValue.matches(regex.asText())) {
				
				if (mappingCase.has("value")) {
					
					String value = mappingCase.get("value").asText();
					
					
					if (value == null || value.equals("null")) {
						
						log.error("Value is null! Mode: direct, "+this.log_info+" TEST: "+mappingCase.get("value").asBoolean());
					}
					
					return value;
					
				} else if (mappingCase.has("constraint")) {
					
					log.warn("Using mode 'direct' for constraint mapping is deprecated, use 'constraint' mode instead! ("+log_info+")");
					
					String type = mappingCase.get("constraint").asText();
					int constValue = MappingHelper.getConstraintValueFromFunctionClassAnnotation(type, propertyName, function);
					
					return Integer.toString(constValue);	
					
				} else {
					log.warn("None of the available mappings for direct mode are fitting ("+log_info+")");
				}
			}
		}
		return null;
	}
	
	private String modeConstraint(JsonNode mappingCase, String unmappedDeviceValue, String propertyName, Object function) {
		ArrayNode regexList = (ArrayNode) mappingCase.get("regex");
		for (JsonNode regex : regexList) {
			if (unmappedDeviceValue.matches(regex.asText())) {
				
				if (mappingCase.has("value")) {
					return mappingCase.get("value").asText();
					
				} else if (mappingCase.has("constraint")) {
					
					log.info("Using keyword 'direct' for constraint mapping is deprecated, use 'constraint' mode instead! ("+log_info+")");
					
					String type = mappingCase.get("constraint").asText();
					int constValue = MappingHelper.getConstraintValueFromFunctionClassAnnotation(type, propertyName, function);
					
					return Integer.toString(constValue);	
					
				} else {
					log.debug("None of the available mappings for direct mode are fitting ("+log_info+")");
				}
			}
		}
		return null;
	}
	
	private String modeRange(JsonNode mappingCase, String unmappedDeviceValue, String propertyName, Object function) {
		
		try {
			double minimumSourceValue = mappingCase.get("minimum").asDouble();
			double maximumSourceValue = mappingCase.get("maximum").asDouble();
			int minimumDestinationValue = MappingHelper.getConstraintValueFromFunctionClassAnnotation("min", propertyName, function);
			int maximumDestinationValue = MappingHelper.getConstraintValueFromFunctionClassAnnotation("max", propertyName, function);
			
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

}
