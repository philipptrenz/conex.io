package mapping.set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.model.Function;
import mapping.MappingHelper;

/**
 * The Class FunctionValueToFHEMValueMapper.
 * 
 * This class maps function values from application format to FHEM format.
 * 
 * @author Philipp Trenz
 */
public class FunctionValueToFHEMValueMapper {
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * Map function value to FHEM value.
	 *
	 * @param propertyName the property name
	 * @param deviceValue the device value
	 * @param valueDescription the value description
	 * @param function the function
	 * @return the string
	 */
	public String mapFunctionValueToFHEMValue(String propertyName, String deviceValue, JsonNode valueDescription, Function function) {
		
		String valueType = valueDescription.get("value_type").asText();
		JsonNode cases = valueDescription.get("cases");
		String mappedValue = null;
		
		for (JsonNode mappingCase : cases) {
			
			
			switch (mappingCase.get("inject_mode").asText()) {
			
			case "direct":
				mappedValue = modeDirect(propertyName, deviceValue, mappingCase, function);
				break;
			
			case "from_to":
				mappedValue = modeFromTo(propertyName, deviceValue, mappingCase, function, valueType);
				break;
				
			case "range":
				mappedValue = modeRange(propertyName, deviceValue, mappingCase, function, valueType);
				break;
				// ...
			}
			
			if (mappedValue != null) {
				return mappedValue;
			}
			
		}
		return null;
	}
	
	/**
	 * Mode direct.
	 *
	 * @param propertyName the property name
	 * @param deviceValue the device value
	 * @param mappingCase the mapping case
	 * @param function the function
	 * @return the string
	 */
	private String modeDirect(String propertyName, String deviceValue, JsonNode mappingCase, Function function) {
		
		if (deviceValue.equals(mappingCase.get("value").asText())) {
			return mappingCase.get("parameter").asText();
		}
		
		return null;
	}
	
	/**
	 * Mode from to.
	 *
	 * @param propertyName the property name
	 * @param functionValue the function value
	 * @param mappingCase the mapping case
	 * @param function the function
	 * @param valueType the value type
	 * @return the string
	 */
	private String modeFromTo(String propertyName, String functionValue, JsonNode mappingCase, Function function, String valueType) {
		
		int minimumDestinationValue = mappingCase.get("minimum").asInt();
		int maximumDestinationValue = mappingCase.get("maximum").asInt();
		
		int minimumSourceValue = MappingHelper.getConstraintValueFromFunctionClassAnnotation("min", propertyName, function);
		int maximumSourceValue = MappingHelper.getConstraintValueFromFunctionClassAnnotation("max", propertyName, function);
		
		double sourceValue = Double.parseDouble(functionValue);
		
		// normalize source value as rational number
	    double rationalSourceValue = (sourceValue-minimumSourceValue) / (maximumSourceValue-minimumDestinationValue);
	    Double destinationValue = rationalSourceValue * (maximumDestinationValue-minimumDestinationValue)+minimumDestinationValue;
	    
	    if (valueType.equals("integer")) {
	    	
	    	for (JsonNode node : mappingCase.get("mapping")) {
				
				int from = node.get("from").asInt();
				int to = node.get("to").asInt();
				
			    int intDestinationValue = destinationValue.intValue();
			    
			    if (intDestinationValue >= from && intDestinationValue <= to) {
			    	return node.get("parameter").asText();
			    }
			}

	    } else {
	    	try {
				throw new Exception("from_to mapping for type "+mappingCase.get("value_type")+" not yet implemented!");
			} catch (Exception e) {
				log.error("valueType is not 'integer'", e);
			}
	    }
	    
		return null;
	}	
	
/**
 * Mode range.
 *
 * @param propertyName the property name
 * @param functionValue the function value
 * @param mappingCase the mapping case
 * @param function the function
 * @param valueType the value type
 * @return the string
 */
private String modeRange(String propertyName, String functionValue, JsonNode mappingCase, Function function, String valueType) {
		
		int minimumDestinationValue = mappingCase.get("minimum").asInt();
		int maximumDestinationValue = mappingCase.get("maximum").asInt();
		
		int minimumSourceValue = MappingHelper.getConstraintValueFromFunctionClassAnnotation("min", propertyName, function);
		int maximumSourceValue = MappingHelper.getConstraintValueFromFunctionClassAnnotation("max", propertyName, function);
		
		double sourceValue = Double.parseDouble(functionValue);
		
		if (sourceValue < minimumSourceValue || sourceValue > maximumSourceValue) return null;
		
		// normalize source value as rational number
	    double rationalSourceValue = (sourceValue-minimumSourceValue) / (maximumSourceValue-minimumDestinationValue);
	    Double destinationValue = rationalSourceValue * (maximumDestinationValue-minimumDestinationValue)+minimumDestinationValue;
	    
	    String prefix = mappingCase.has("prefix") ? mappingCase.get("prefix").asText() : null;
	    String suffix = mappingCase.has("suffix") ? mappingCase.get("suffix").asText() : null;
	    
	    if (valueType.equals("integer")) {
	    	
	    	return Integer.toString(destinationValue.intValue());

	    } else if (valueType.equals("number")){ 
	    	
	    	return Float.toString(destinationValue.floatValue());
	    	
	    }else {
	    	log.warn("Mode 'from_to' for type "+valueType+" not yet implemented!");
	    }
	    
		return null;
	}	
}
