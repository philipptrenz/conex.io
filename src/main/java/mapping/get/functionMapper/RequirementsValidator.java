package mapping.get.functionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import mapping.MappingHelper;
import mapping.exceptions.MalformedFHEMModuleDescriptionJsonException;
import mapping.exceptions.NoValidKeyPathException;

/**
 * The Class MappingRequirementsValidator.
 * 
 * This class validates the incoming FHEM device from jsonlist2 as JSON to fit the requirements
 * to get mapped defined in the FHEM module description (json file).
 * 
 * @author Philipp Trenz
 */
public class RequirementsValidator {
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Check if via jsonlist2 received device fits requirements of module description for mapping.
	 * 
	 * If more than one requirement is defined, they all have to be valid  for the jsonDevice (AND)
	 *
	 * @param mappingRequirements the mapping requirements
	 * @param jsonlist2Device the jsonlist 2 device
	 * @return true, if successful
	 * @throws MalformedFHEMModuleDescriptionJsonException the malformed FHEM module description json exception
	 */
	public boolean doFitRequirements(JsonNode mappingRequirements, JsonNode jsonlist2Device) throws MalformedFHEMModuleDescriptionJsonException {
		
		if (mappingRequirements == null || !mappingRequirements.isArray()) throw new MalformedFHEMModuleDescriptionJsonException();
		
		for (JsonNode requirement : mappingRequirements) {			
			
			String mode = requirement.get("mode").asText();
			boolean fitsRequirements;
			
			switch (mode) {
			
			case "contains_all": 
				fitsRequirements = isModeContainsAll(requirement, jsonlist2Device);
				break;
			case "one_of": 
				fitsRequirements = isModeOneOf(requirement, jsonlist2Device);		
				break;
			default: 
				log.debug("Requirements mode '"+mode+"' is empty, so it's okay");
				fitsRequirements = true;
				break;
				
			}
			
			if (fitsRequirements) {
				continue;
			} else {
				return false;
			}
			
		}
		
		return true;
	}
	
	/**
	 * Checks if is mode contains_all.
	 *
	 * @param requirement the requirement
	 * @param jsonlist2Device the jsonlist2 device
	 * @return true, if is mode contains all
	 */
	private boolean isModeContainsAll(JsonNode requirement, JsonNode jsonlist2Device) {
		String path = requirement.get("key_path").asText();
		try {
			String jsonlist2InputValue = MappingHelper.navigateJsonKeyPath(jsonlist2Device, path).asText();
			
			if (jsonlist2InputValue.isEmpty()) {
				return false;
			}
			
			JsonNode array = requirement.get("attributes");
			String delimiters;
			try {
				delimiters = requirement.get("delimiters").asText();
			} catch (NullPointerException e) {
				delimiters = null;
			}
			
			
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
	
	/**
	 * Checks if is mode one_of.
	 *
	 * @param requirement the requirement
	 * @param jsonlist2Device the jsonlist 2 device
	 * @return true, if is mode one of
	 */
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
