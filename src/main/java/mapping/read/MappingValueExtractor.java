package mapping.read;

import java.lang.reflect.Method;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import mapping.exceptions.NoValidKeyPathException;

/**
 * The Class MappingValueExtractor.
 * 
 * This class validates the incoming FHEM device from jsonlist2 as JSON to fit the requirements
 * to get mapped defined in the FHEM module description (json file).
 */
public class MappingValueExtractor {
	
	/**
	 * Extract value.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @param property the property
	 * @return the string
	 */
	
	private Object function;
	private String propertyName;
	private JsonNode jsonlist2Device;
	
	public String extractValue(JsonNode jsonlist2Device, JsonNode property, String propertyName, Object function) {		
		
		this.function = function;
		this.propertyName = propertyName;
		this.jsonlist2Device = jsonlist2Device;
		
		
		String key_path = property.get("key_path").asText();
		String unmappedDeviceValue;
		try {
			unmappedDeviceValue = MappingReadHelper.navigateJsonKeyPath(jsonlist2Device, key_path).asText();
			
			ArrayNode cases = (ArrayNode) property.get("cases");
			for (JsonNode mappingCase : cases) {
				
				String extractMode = mappingCase.get("extract_mode").asText();
				switch (extractMode) {
				
				case "direct": 
					String value = modeDirect(mappingCase, unmappedDeviceValue, function);
					if (value != null && !value.isEmpty()) {
						return value;
					}
				
				case "range":
					System.out.println("extraction_mode 'range' not yet implemented");
					break;
					
				default:
					System.out.println("extract_mode not found, maybe function description is made for newer version of conex.io core");
					break;
				}
				
			}
		} catch (NoValidKeyPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private String modeDirect(JsonNode mappingCase, String unmappedDeviceValue, Object function) {
		ArrayNode regexList = (ArrayNode) mappingCase.get("regex");
		for (JsonNode regex : regexList ) {
			if (unmappedDeviceValue.matches(regex.asText())) {
				
				
				if (mappingCase.has("value")) {
					
					return mappingCase.get("value").asText();
					
				} else if (mappingCase.has("constraint")) {
					
					String type = mappingCase.get("constraint").asText();
					int constValue = getConstraintValueFromFunctionClassAnnotation(type);
					
					System.out.println("setting from constraint '"+type+"' value to "+constValue);
					
					return Integer.toString(constValue);
					
				}
				
			}
		}
		return null;
	}
	
	private int getConstraintValueFromFunctionClassAnnotation(String type) {
		Method[] methods = function.getClass().getMethods();
		Method setMethod = null;
		for (Method m : methods) {
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
		return 0;
	}

}
