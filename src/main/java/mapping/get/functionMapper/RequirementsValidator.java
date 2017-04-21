package mapping.get.functionMapper;

import com.fasterxml.jackson.databind.JsonNode;

import mapping.MappingHelper;
import mapping.exceptions.MalformedFHEMModuleDescriptionJsonException;
import mapping.exceptions.NoValidKeyPathException;

/**
 * The Class MappingRequirementsValidator.
 * 
 * This class validates the incoming FHEM device from jsonlist2 as JSON to fit the requirements
 * to get mapped defined in the FHEM module description (json file).
 */
public class RequirementsValidator {
	
	
	/**
	 * Do fit requirements.
	 *
	 * @param mappingDescription the mapping description
	 * @param jsonlist2Device the jsonlist 2 device
	 * @return true, if successful
	 * @throws MalformedFHEMModuleDescriptionJsonException 
	 */
	public boolean doFitRequirements(JsonNode mappingRequirements, JsonNode jsonlist2Device) throws MalformedFHEMModuleDescriptionJsonException {
		
		if (mappingRequirements == null || !mappingRequirements.isArray()) throw new MalformedFHEMModuleDescriptionJsonException();
		
		for (JsonNode requirement : mappingRequirements) {			
			
			String mode = requirement.get("mode").asText();
			
			switch (mode) {
			
			case "contains_all": return isModeContainsAll(requirement, jsonlist2Device);
			
			case "one_of": return isModeOneOf(requirement, jsonlist2Device);
			
			default: System.out.println("mode '"+mode+"' not found");
				
			}
			
		}
		
		// TODO Auto-generated method stub
		// use MappingRequirementsValidator class for implementing!
		// iterate through requirements key in module description and check every requirements item
		// for now: "contains on key" - Checking value on key with regex
		
		return true;
	}
	
	private boolean isModeContainsAll(JsonNode requirement, JsonNode jsonlist2Device) {
		String path = requirement.get("key_path").asText();
		try {
			String jsonlist2InputValue = MappingHelper.navigateJsonKeyPath(jsonlist2Device, path).asText();
			
			if (jsonlist2InputValue.isEmpty()) {
				return false;
			}
			
			JsonNode array = requirement.get("attributes");
			String delimiters = requirement.get("delimiters").asText();
			
			for (final JsonNode node : array) {
				String value = node.asText();
				String regex = ".*(^|["+delimiters+"])("+value+")($|["+delimiters+"]).*";
				
				if (!jsonlist2InputValue.matches(regex)) {
					return false;
				}
			}
			
			
		} catch (NoValidKeyPathException e) {
			return false;
		}
		return true;
	}	
	
	private boolean isModeOneOf(JsonNode requirement, JsonNode jsonlist2Device) {
		String path = requirement.get("key_path").asText();
		try {
			String jsonlist2InputValue = MappingHelper.navigateJsonKeyPath(jsonlist2Device, path).asText();
			
			if (jsonlist2InputValue.isEmpty()) {
				return false;
			}
			
			JsonNode array = requirement.get("attributes");
			
			for (final JsonNode node : array) {
				String value = node.asText();
				if (jsonlist2InputValue.equals(value)) return true;
			}
			return false;
			
			
		} catch (NoValidKeyPathException e) {
			return false;
		}
		
	}	
	
	
	
}
