package mapping.get.functionMapper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.MappingHelper;
import mapping.get.WebsocketDeviceUpdateMessage;

/**
 * The Class FunctionMapper.
 * 
 * This class maps the FHEM values to function models (OnOff, Dimmer, ...).
 * 
 * @author Philipp Trenz
 */
public class FunctionMapper {
	
	/** The mapper. */
	private ObjectMapper mapper;
	
	/** The requirements validator. */
	private RequirementsValidator requirementsValidator;
	
	/** The extractor. */
	private ValueExtractor extractor;
	
	/** The mapping helper. */
	@Autowired
    public MappingHelper mappingHelper;
	
	/** The log info. */
	private String log_info = "";
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Instantiates a new function mapper.
	 */
	public FunctionMapper() {
		this.mapper = new ObjectMapper();
		this.requirementsValidator = new RequirementsValidator();
		this.extractor = new ValueExtractor();
		
		// register Joda module for org.joda.time.DateTime parsing
		mapper.registerModule(new JodaModule());
	}
	
	/**
	 * Map json to functions.
	 *
	 * @param json the json
	 * @param moduleDescription the module description
	 * @param log_info the log info
	 * @return the list
	 */
	/*
	 * This function maps values from jsonlist2 to conex.io Functions
	 */
	public List<Function> mapJsonToFunctions(JsonNode json, JsonNode moduleDescription, String log_info)  {
		
		this.log_info = log_info;
		
		List<Function> list = new ArrayList<>();
		
		// choose read part for FHEM to Java Mapping
		ArrayNode readDescription = (ArrayNode) moduleDescription.get("functions").get("get");
		
		for (JsonNode funcDescription : readDescription) {
			
			Function function = mapFunction(funcDescription, json);
			if (function != null) list.add(function);
		}
		
		return list;
	}
	
	/**
	 * Map function.
	 *
	 * @param funcDescription the func description
	 * @param json the json
	 * @return the function
	 */
	private Function mapFunction(JsonNode funcDescription, JsonNode json){

		try {
			
			// 0. Check if jsonlist2 data fits requirements
			if (requirementsValidator.doFitRequirements(funcDescription.get("requirements"), json)) {
			
				String functionId = funcDescription.get("function_id").asText();				
				
				Class<?> functionClass = mappingHelper.findFunctionClassByFunctionId(functionId);
				Object function = functionClass.newInstance();
				ObjectNode proto = mapper.valueToTree(function);
				
				
				// 2. Insert values from jsonlist2 to prototype
				
				// iterate over all properties to get the values mapped
				for(JsonNode property : funcDescription.get("properties")) {
					String key = property.get("value_name").asText();
					String value = extractor.extractValue(json, property, key, function, log_info);
					if (value != null) {
						proto.put(key, value);
					}
				}
				proto.put("function_id", functionId);
				
				// 3. Map back to Java object
				function = mapper.treeToValue(proto, functionClass);
				return (Function) function;
				
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("While Function mapping an error occured ("+log_info+")", e);
			return null;
		}
			
	}
	
	/**
	 * Map websocket values to function.
	 *
	 * @param device the device
	 * @param message the message
	 * @param moduleDescription the module description
	 * @param log_info the log info
	 * @return true, if successful
	 */
	/*
	 * This function maps values from websocket connection to conex.io Functions
	 */
	public boolean mapWebsocketValuesToFunction(Device device, WebsocketDeviceUpdateMessage message, JsonNode moduleDescription, String log_info)  {
		
		this.log_info = log_info;
		
		boolean updated = false;
		final List<Function> functionsList = device.getFunctions();
		
		// choose read part for FHEM to Java Mapping
		ArrayNode readDescription = (ArrayNode) moduleDescription.get("functions").get("get");
		
		List<Function> updatedList = new ArrayList<>(functionsList);
		
		for (Function f : functionsList) {
			
			for (JsonNode funcDescription : readDescription) {	
				
				String functionId = funcDescription.get("function_id").asText();
				
				Class<?> clazz = MappingHelper.findFunctionClassByFunctionId(functionId);
				
				// if device has this Function
				if (clazz != null && f.getClass().equals(clazz)) {
					
					// yey, this Function looks correct						
					boolean valueMapped = false;
					boolean timestampMapped = false;
					
					for (JsonNode prop : funcDescription.get("properties")) {
						String key = prop.get("value_name").asText();
						String keyPath = prop.get("key_path").asText().toLowerCase();
						
						// if reading is defined in this Function
						if (keyPath.startsWith("readings/"+message.reading.toLowerCase())) {

							String value = null;
							if (keyPath.equals("readings/"+message.reading.toLowerCase()+"/value")) {								
								value = extractor.extractValue(message.value, prop, key, f, log_info);
								valueMapped = true;
							} else if (keyPath.equals("readings/"+message.reading.toLowerCase()+"/time")) {
								value = extractor.extractValue(message.timestamp, prop, key, f, log_info);
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
									log.error("Mapping json to Function failed ("+log_info+")", e);
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
