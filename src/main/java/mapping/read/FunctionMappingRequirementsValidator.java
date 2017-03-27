package mapping.read;

import com.fasterxml.jackson.databind.JsonNode;

import mapping.exceptions.MalformedFHEMModuleDescriptionJsonException;
import mapping.exceptions.NoValidKeyPathException;

/**
 * The Class MappingRequirementsValidator.
 * 
 * This class validates the incoming FHEM device from jsonlist2 as JSON to fit the requirements
 * to get mapped defined in the FHEM module description (json file).
 */
public class FunctionMappingRequirementsValidator {
	
	
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
			// mode: "contains_all"
			if (requirement.get("mode").asText().equals("contains_all")) {
				
				String path = requirement.get("key_path").asText();
				try {
					String jsonlist2InputValue = navigateJsonKeyPath(jsonlist2Device, path).asText();
					
					if (jsonlist2InputValue.isEmpty()) {
						System.out.println("Function can't be validated, needed value at key path '"+path+"' is missing");
						return false;
					}
					
					
					
					JsonNode array = requirement.get("attributes");
					String delimiters = requirement.get("delimiters").asText();
					
					for (final JsonNode node : array) {
						String value = node.asText();
						String regex = ".*(^|["+delimiters+"])("+value+")($|["+delimiters+"]).*";
						
						if (!jsonlist2InputValue.matches(regex)) {
							
							System.out.println("Not matching regex: '"+regex+ "'\tvalue: '"+jsonlist2InputValue+"'");
							return false;
						}
					}
					
					
				} catch (NoValidKeyPathException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
			
			
			
			
			
			
			
			
		}
		
		// TODO Auto-generated method stub
		// use MappingRequirementsValidator class for implementing!
		// iterate through requirements key in module description and check every requirements item
		// for now: "contains on key" - Checking value on key with regex
		
		return true;
	}
	
	private JsonNode navigateJsonKeyPath(JsonNode node, String path) throws NoValidKeyPathException {
		// remove whitespaces, linebreaks etc
		path = path.replaceAll("\\r\\n|\\r|\\n", "").replace(" ", "");
		String[] keys = path.split("/");
		
		JsonNode temp = node;
		for (String key : keys) {			
			if (!temp.has(key)) throw new NoValidKeyPathException();
			temp = temp.get(key);
		}
		
		return temp;
	}
	
	
}
