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
import mapping.get.ModuleDescriptionLoader;

public class FHEMCommandBuilder {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ModuleDescriptionLoader loader;
	private ObjectMapper mapper;
	
	private FunctionValueToFHEMValueMapper functionToFHEMValueMapper;
	
	public FHEMCommandBuilder() {
		
		this.loader = new ModuleDescriptionLoader();
		this.mapper = new ObjectMapper();
		mapper.registerModule(new JodaModule());
		
		this.functionToFHEMValueMapper = new FunctionValueToFHEMValueMapper();
	}
	
	public String buildCommand(List<Device> devices, Function functionValuesToSet) {
		
		if (functionValuesToSet != null) {
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
		} else {
			return null;
		}
	}
	
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
			
			if (key.equals("function_id")) continue;
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
	
	private JsonNode getFunctionDescriptionForSet(Function function, String deviceType) {
		JsonNode moduleDescription = loader.getModuleDescription(deviceType);
		JsonNode setSection = moduleDescription.get("functions").get("set");
		
		for (JsonNode functionDescriptionSet : setSection) {
			String className = functionDescriptionSet.get("class_name").asText();
			if (className.contains(function.getClass().getSimpleName()) || function.getClass().getName().contains(className)) {
				return functionDescriptionSet;
			} else {
				continue;
			}
		}
		return null;
	}
	
	private Map<String, String> concatDeviceIdsByTypeId(List<Device> deviceList, Class<?> functionClass){
		Map<String, String> deviceIdsByTypeIdMap = new HashMap<>();
		for (Device device : deviceList) {
			boolean containsFunction = false;
			for (Function function : device.getFunctions()) {
				if (function.getClass().equals(functionClass)){
					containsFunction = true;
					break;
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
