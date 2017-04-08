package mapping.get.functionMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.model.Function;

public class FunctionMapper {
	
	private ObjectMapper mapper;
	private RequirementsValidator requirementsValidator;
	private ValueExtractor extractor;
	private JsonNode json;
	private JsonNode moduleDescription;
	
	
	public FunctionMapper(JsonNode json, JsonNode moduleDescription) {
		this.json = json;
		this.moduleDescription = moduleDescription;
		
		this.mapper = new ObjectMapper();
		this.requirementsValidator = new RequirementsValidator();
		this.extractor = new ValueExtractor();
	}
	
	/*
	 * This function maps values from jsonlist2 to conex.io Functions
	 */
	public List<Function> mapJsonToFunctions()  {
		
		List<Function> list = new ArrayList<>();
		
		// choose read part for FHEM to Java Mapping
		ArrayNode readDescription = (ArrayNode) moduleDescription.get("functions").get("get");
		
		for (JsonNode funcDescription : readDescription) {
			
			Function function = mapFunction(funcDescription);
			if (function != null) list.add(function);
		}
		
		return list;
		
	}
	
	private Function mapFunction(JsonNode funcDescription){

		try {
			
			// 0. Check if jsonlist2 data fits requirements
			if (requirementsValidator.doFitRequirements(funcDescription.get("requirements"), json)) {
			
				String functionId = funcDescription.get("function_id").asText();
				
				// 1. Generate prototype JSON as JsonNode from Java Class by classname
				String className = funcDescription.get("class_name").asText();
				Class<?> functionClass = Class.forName(className);
				Object function = functionClass.newInstance();
				ObjectNode proto = mapper.valueToTree(function);
				
				
				// 2. Insert values from jsonlist2 to prototype
				
				// iterate over all properties to get the values mapped
				Iterator<Map.Entry<String, JsonNode>> fields;
				fields = funcDescription.get("properties").fields();
				while (fields.hasNext()) {
					Map.Entry<String, JsonNode> entry = fields.next();
					String key = entry.getKey();
					JsonNode property = entry.getValue();
					
					String value = extractor.extractValue(json, property, key, function);
					proto.put(key, value);
				}
				proto.put("function_id", functionId);
				
				
				// 3. Map back to Java object
				
				function = mapper.treeToValue(proto, Class.forName(className));
				return (Function) function;
				
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
			
	}
	
}
