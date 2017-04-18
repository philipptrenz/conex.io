package mapping.get.functionMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.omg.CosNaming._BindingIteratorImplBase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.get.WebsocketDeviceUpdateMessage;

public class FunctionMapper {
	
	private ObjectMapper mapper;
	private RequirementsValidator requirementsValidator;
	private ValueExtractor extractor;
	
	
	public FunctionMapper() {
		this.mapper = new ObjectMapper();
		this.requirementsValidator = new RequirementsValidator();
		this.extractor = new ValueExtractor();
		
		// register Joda module for org.joda.time.DateTime parsing
		mapper.registerModule(new JodaModule());
	}
	
	/*
	 * This function maps values from jsonlist2 to conex.io Functions
	 */
	public List<Function> mapJsonToFunctions(JsonNode json, JsonNode moduleDescription)  {
		
		List<Function> list = new ArrayList<>();
		
		// choose read part for FHEM to Java Mapping
		ArrayNode readDescription = (ArrayNode) moduleDescription.get("functions").get("get");
		
		for (JsonNode funcDescription : readDescription) {
			
			Function function = mapFunction(funcDescription, json);
			if (function != null) list.add(function);
		}
		
		return list;
	}
	
	private Function mapFunction(JsonNode funcDescription, JsonNode json){

		try {
			
			// 0. Check if jsonlist2 data fits requirements
			if (requirementsValidator.doFitRequirements(funcDescription.get("requirements"), json)) {
			
				String functionId = funcDescription.get("function_id").asText();
				
				// 1. Generate prototype JSON as JsonNode from Java Class by classname
				String className = funcDescription.get("class_name").asText();
				Class<Function> functionClass = (Class<Function>) Class.forName(className);
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
	
	/*
	 * This function maps values from websocket connection to conex.io Functions
	 */
	public boolean mapWebsocketValuesToFunction(Device device, WebsocketDeviceUpdateMessage message, JsonNode moduleDescription)  {
		
		boolean updated = false;
		final List<Function> functionsList = device.getFunctions();
		
		// choose read part for FHEM to Java Mapping
		ArrayNode readDescription = (ArrayNode) moduleDescription.get("functions").get("get");
		
		List<Function> updatedList = new ArrayList<>(functionsList);
		
		for (Function f : functionsList) {
			
			for (JsonNode funcDescription : readDescription) {	
				
				Class<Function> clazz = null;
				try {
					clazz = (Class<Function>) Class.forName(funcDescription.get("class_name").asText());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// if device has this Function
				if (clazz != null && f.getClass().equals(clazz)) {
					
					// yey, this Function looks correct						
					boolean valueMapped = false;
					boolean timestampMapped = false;
											
					Iterator<Map.Entry<String, JsonNode>> properties;
					properties = funcDescription.get("properties").fields();
					
					while (properties.hasNext()) {
						Map.Entry<String, JsonNode> entry = properties.next();
						String key = entry.getKey();
						JsonNode prop = entry.getValue();
						
						String keyPath = prop.get("key_path").asText().toLowerCase();
						
						// if reading is defined in this Function
						if (keyPath.startsWith("readings/"+message.reading.toLowerCase())) {

							String value = null;
							if (keyPath.equals("readings/"+message.reading.toLowerCase()+"/value")) {								
								value = extractor.extractValue(message.value, prop, key, f);
								valueMapped = true;
							} else if (keyPath.equals("readings/"+message.reading.toLowerCase()+"/time")) {
								value = extractor.extractValue(message.timestamp, prop, key, f);
								timestampMapped = true;
							} else {
								value = null;
							}
							
							if (value != null && !value.isEmpty()) {
								ObjectNode proto = mapper.valueToTree(updatedList.get(functionsList.indexOf(f)));
								
								proto.put(key, value);
								Function updatedFunction;
								try {
									updatedFunction = (Function) mapper.treeToValue(proto, clazz);
									
									// replace in device
									updatedList.set(functionsList.indexOf(f), updatedFunction);
									
									updated = true;
								} catch (JsonProcessingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			
		}
		
		if (updated) {
			device.setFunctions(updatedList);
		}
		
		return updated;
	}

}
