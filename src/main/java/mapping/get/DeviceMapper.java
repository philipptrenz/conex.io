package mapping.get;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.MappingHelper;
import mapping.get.functionMapper.FunctionMapper;

/*
 * Class to load FHEM module descriptions
 * 
 * TODO: Add caching for reduced IO-accesses
 */
public class DeviceMapper {
	
	private FunctionMapper funcMapper;
	private ModuleDescriptionLoader loader;
	
	/*
	 * Constructor
	 */
	public DeviceMapper(ModuleDescriptionLoader loader) {
		this.loader = loader;
		this.funcMapper = new FunctionMapper();
	}
	
	/*
	 * 
	 */
	public Device mapJsonToDevice(JsonNode jsonlist2Device)  {
		
		Device newDevice = new Device();
	
		JsonNode moduleDescription = loader.getModuleDescription(jsonlist2Device);
		
		// map deviceId
		newDevice.setDeviceId(getDeviceId(jsonlist2Device, moduleDescription));
		
		// map typeId
		newDevice.setTypeId(getTypeId(jsonlist2Device, moduleDescription));
		
		// map roomIds
		newDevice.setRoomIds(getRoomIds(jsonlist2Device, moduleDescription));
		
		// map groupIds
		newDevice.setGroupIds(getGroupIds(jsonlist2Device, moduleDescription));
		
		// map functions
		
		newDevice.setFunctions(funcMapper.mapJsonToFunctions(jsonlist2Device, moduleDescription));
		
		if (newDevice.getDeviceId() != null) return newDevice;
		System.out.println("device_id mapping failed");
		return null;
	}
	
	
	
	private String getDeviceId(JsonNode jsonlist2Device, JsonNode moduleDescription) {
		JsonNode mappingDescription = moduleDescription.get("device_id");
		String id = null;
		try {
			String keyPath = mappingDescription.get("key_path").asText();
			JsonNode type = MappingHelper.navigateJsonKeyPath(jsonlist2Device, keyPath);
			id = type.asText().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	private String getTypeId(JsonNode jsonlist2Device, JsonNode moduleDescription) {
		JsonNode mappingDescription = moduleDescription.get("type_id");
		String id = null;
		try {
			String keyPath = mappingDescription.get("key_path").asText();
			JsonNode type = MappingHelper.navigateJsonKeyPath(jsonlist2Device, keyPath);
			id = type.asText().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	private List<String> getRoomIds(JsonNode jsonlist2Device, JsonNode moduleDescription) {
		if (moduleDescription.has("rooms")) {
			JsonNode mappingDescription = moduleDescription.get("rooms");
			
			String string;
			try {
				string = MappingHelper.navigateJsonKeyPath(jsonlist2Device, mappingDescription.get("key_path").asText()).asText();
			
				List<String> list = new ArrayList<>();
				if (mappingDescription.has("delimiters") && !mappingDescription.get("delimiters").asText().isEmpty()) {
					String delimiter = mappingDescription.get("delimiters").asText();
					String[] array = string.split(delimiter);
					Collections.addAll(list, array);
				} else {
					list.add(string);
				}
				
				return list;
			} catch (Exception e) {
				return new ArrayList<>();
			}
		} else {
			return new ArrayList<>();
		}
	}
	
	
	private List<String> getGroupIds(JsonNode jsonlist2Device, JsonNode moduleDescription) {
		if (moduleDescription.has("groups")) {
			JsonNode mappingDescription = moduleDescription.get("groups");
			
			String string;
			try {
				string = MappingHelper.navigateJsonKeyPath(jsonlist2Device, mappingDescription.get("key_path").asText()).asText();
				
				List<String> list = new ArrayList<>();
				if (mappingDescription.has("delimiters") && !mappingDescription.get("delimiters").asText().isEmpty()) {
					String delimiter = mappingDescription.get("delimiters").asText();
					String[] array = string.split(delimiter);
					Collections.addAll(list, array);
				} else {
					list.add(string);
				}
				
				return list;
			} catch (Exception e) {
				return new ArrayList<>();
			}
		} else {
			return new ArrayList<>();
		}
	}

}
