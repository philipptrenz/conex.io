package mapping.set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.model.Function;
import mapping.MappingHelper;

public class FunctionValueToFHEMValueMapper {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

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
	
	private String modeDirect(String propertyName, String deviceValue, JsonNode mappingCase, Function function) {
		
		if (deviceValue.equals(mappingCase.get("value").asText())) {
			return mappingCase.get("parameter").asText();
		}
		
		return null;
	}
	
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
		return null;
	}	
	
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
