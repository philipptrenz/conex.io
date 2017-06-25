package mapping.set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.MappingHelper;
import mapping.get.ModuleDescriptionLoader;

/**
 * The Class FHEMCommandBuilder.
 * 
 * This class generates valid FHEM commands based on retrieved filter and function objects 
 * at the 'PATCH /devices' endpoint and the module function descriptions.
 * 
 * @author Philipp Trenz
 */
public class FHEMCommandBuilder {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/** The loader. */
	private ModuleDescriptionLoader loader;
	
	/** The mapper. */
	private ObjectMapper mapper;
	
	/** The function to FHEM value mapper. */
	private FunctionValueToFHEMValueMapper functionToFHEMValueMapper;
	
	/**
	 * Instantiates a new FHEM command builder.
	 */
	public FHEMCommandBuilder() {
		
		this.loader = new ModuleDescriptionLoader();
		this.mapper = new ObjectMapper();
		mapper.registerModule(new JodaModule());
		
		this.functionToFHEMValueMapper = new FunctionValueToFHEMValueMapper();
	}

	/**
	 * Builds the command.
	 *
	 * @param devices the devices
	 * @param functionValuesToSet the function values to set
	 * @return the string
	 */
	public String buildCommand(List<Device> devices, Function functionValuesToSet) {
			
		if (functionValuesToSet == null) return null;
		
		List<String> commands = new ArrayList<>();
		Map<String, String> deviceIdsByTypeIdMap = concatDeviceIdsByTypeId(devices, functionValuesToSet.getClass());
		
		Iterator it = deviceIdsByTypeIdMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	
	    	String moduleCommand = "";
	    	
	        Map.Entry pair = (Map.Entry) it.next();
	        
	        String deviceType = (String) pair.getKey();
	        String concatenatedDeviceIds = (String) pair.getValue();
	        
	        //log.info("Setting "+functionValuesToSet.getClass().getSimpleName()+" for "+concatenatedDeviceIds.replace(",", ", ")+" (type_id: "+deviceType+")");
	 
	        List<String> commandsForDeviceType = buildCommandForValues(functionValuesToSet, concatenatedDeviceIds, deviceType);
	        commands.addAll(commandsForDeviceType);

	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
		return concatCommandList(commands);
	}
	
	/**
	 * Concatenate command list.
	 *
	 * @param commandList the command list
	 * @return the string
	 */
	private String concatCommandList(List<String> commandList) {
		
		String command = "";
		for (String cmd : commandList) {
			if (!command.isEmpty()) {
				command += "; ";
			}
			command += cmd;
		}
		return command;
	}
	
	/**
	 * Builds the command for values.
	 *
	 * @param function the function
	 * @param concatenatedDeviceIds the concatenated device ids
	 * @param deviceType the device type
	 * @return the list
	 */
	private List<String> buildCommandForValues(Function function, String concatenatedDeviceIds, String deviceType){
		
		List<String> commands = new ArrayList<>();
		if (!loader.moduleDescriptionExists(deviceType)) return commands;
		JsonNode functionToSet = mapper.valueToTree(function);
		JsonNode functionDescription = getFunctionDescriptionForSet(function, deviceType);
		
		if (functionDescription == null) return commands;
		
		JsonNode descriptionProperties = functionDescription.get("properties");
		
		Iterator<Entry<String, JsonNode>> nodes = functionToSet.fields();
		while (nodes.hasNext()) {
			Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodes.next();
			String key = entry.getKey();
			String value = entry.getValue().asText();
			
			if (key.equalsIgnoreCase("function_id")) continue;
			if (entry.getValue().isNull()) continue;
				
			JsonNode valueDescription = getValueDescription(key, descriptionProperties);
			
			if (valueDescription == null) continue;
			
			String command = "";
			
			command += valueDescription.get("command_type").asText()+" ";
			
			command += concatenatedDeviceIds+" ";
			
			if (valueDescription.has("reading")) {
				String reading = valueDescription.get("reading").asText();
				if (!reading.isEmpty()) {
					command += valueDescription.get("reading").asText()+" ";
				}
			}
			
			String fhemValue = functionToFHEMValueMapper.mapFunctionValueToFHEMValue(key, value, valueDescription, function);
			
			if(fhemValue == null) continue;
			command += fhemValue;
			
			commands.add(command);
		}

		return commands;
	}
	
	/**
	 * Gets the value description.
	 *
	 * @param valueName the value name
	 * @param descriptionProperties the description properties
	 * @return the value description
	 */
	private JsonNode getValueDescription(String valueName, JsonNode descriptionProperties) {
		for (JsonNode property : descriptionProperties) {
			if (property.get("value_name").asText().equals(valueName)) {
				return property;
			} else {
				continue;
			}
		}
		return null;
	}
	
	/**
	 * Gets the function description for setting properties.
	 *
	 * @param function the function
	 * @param deviceType the device type
	 * @return the function description for set
	 */
	private JsonNode getFunctionDescriptionForSet(Function function, String deviceType) {
		JsonNode moduleDescription = loader.getModuleDescription(deviceType);
		JsonNode setSection = moduleDescription.get("functions").get("set");
		
		for (JsonNode functionDescriptionSet : setSection) {
			String className = functionDescriptionSet.get("function_id").asText();
			
			if (className.equalsIgnoreCase(function.getClass().getSimpleName())) {
				return functionDescriptionSet;
			} else {
				continue;
			}
		}
		return null;
	}
	
	/**
	 * Concatenates device ids by type_id.
	 *
	 * @param deviceList the device list
	 * @param functionClass the function class
	 * @return the map
	 */
	private Map<String, String> concatDeviceIdsByTypeId(List<Device> deviceList, Class<?> functionClass){
		Map<String, String> deviceIdsByTypeIdMap = new HashMap<>();
		for (Device device : deviceList) {
			boolean containsFunction = false;
			for (Function function : device.getFunctions()) {
				if (function.getClass().equals(functionClass)){
					containsFunction = true;
					break;
				} else if (functionClass.isInstance(function)) {
					if (!functionClass.getSimpleName().equalsIgnoreCase("Function")) {
						containsFunction = true;
						break;
					}
				}
			}
			if (containsFunction) {
				String typeId = device.getTypeId();
				
				String concatString = "";
				if (deviceIdsByTypeIdMap.containsKey(typeId)) {
					concatString = deviceIdsByTypeIdMap.get(typeId);
				}
				
				if (!concatString.isEmpty()) {
					// separate by ","
					concatString += ",";
				}
				concatString += device.getDeviceId();
				deviceIdsByTypeIdMap.put(typeId, concatString);
			}
		}
		return deviceIdsByTypeIdMap;
	}
}
