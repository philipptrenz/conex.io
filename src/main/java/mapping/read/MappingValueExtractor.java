package mapping.read;


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
	public String extractValue(JsonNode jsonlist2Device, JsonNode property, String attributeName) {
		
		String extractMode = property.get("extract_mode").asText();
		switch (extractMode) {
		
		case "bool": return modeBool(property, jsonlist2Device);
		
		default:
			System.out.println("extract_mode not found, maybe function description is made for newer version of conex.io core");
		}
		
		/*
		 * TODO: 
		 * 1. look which extract_mode is given and to which type will be mapped
		 * 2. choose correct extraction method
		 * 3. extract and convert value to Java (for validation)
		 * 4. convert to string and return
		 * 
		 */
		
		return "";
	}
	
	private String modeBool(JsonNode property, JsonNode jsonlist2Device) {
		String result = property.get("default").asText();
		String key_path = property.get("key_path").asText();
		
		String unmappedDeviceValue = "";
		try {
			unmappedDeviceValue = MappingReadHelper.navigateJsonKeyPath(jsonlist2Device, key_path).asText();
		} catch (NoValidKeyPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayNode cases = (ArrayNode) property.get("cases");
		
		// iterate over all cases and test 
		for (JsonNode node : cases) {
			String value = node.get("value").asText();
			
			// iterate regex for testing
			ArrayNode regexList = (ArrayNode) node.get("regex");
			for (JsonNode regex : regexList) {
				if (unmappedDeviceValue.matches(regex.asText())) {
					return value;
				}
			}
		}
		
		return result;
	}

}
