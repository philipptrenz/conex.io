package mapping.get;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.model.Device;
import mapping.MappingHelper;
import mapping.exceptions.NoValidKeyPathException;
import mapping.get.functionMapper.FunctionMapper;

/**
 * The Class DeviceMapper.
 * 
 * This class maps the retrieved JSON objects to models.
 * 
 * @author Philipp Trenz
 */
public class DeviceMapper {
	
	/** The func mapper. */
	private FunctionMapper funcMapper;
	
	/** The loader. */
	private ModuleDescriptionLoader loader;
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/** The log info. */
	private String log_info = "";
	
	/**
	 * Instantiates a new device mapper.
	 *
	 * @param loader the loader
	 */
	public DeviceMapper(ModuleDescriptionLoader loader) {
		this.loader = loader;
		this.funcMapper = new FunctionMapper();
	}
	
	/**
	 * Map json to device.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @return the device
	 */
	public Device mapJsonToDevice(JsonNode jsonlist2Device)  {
		
		Device newDevice = new Device();
	
		JsonNode moduleDescription = loader.getModuleDescription(jsonlist2Device);
		log_info = "type: "+jsonlist2Device.get("Internals").get("TYPE").asText();
		
		// map deviceId
		newDevice.setDeviceId(getDeviceId(jsonlist2Device, moduleDescription));
		
		// map typeId
		newDevice.setTypeId(getTypeId(jsonlist2Device, moduleDescription));
		
		// map roomIds
		newDevice.setRoomIds(getRoomIds(jsonlist2Device, moduleDescription));
		
		// map groupIds
		newDevice.setGroupIds(getGroupIds(jsonlist2Device, moduleDescription));
		
		// map functions
		newDevice.setFunctions(funcMapper.mapJsonToFunctions(jsonlist2Device, moduleDescription, log_info));
		
		// do not return broken devices or one without functions
		if (newDevice.getDeviceId() == null || newDevice.getDeviceId().isEmpty() 
				|| newDevice.getTypeId() == null || newDevice.getTypeId().isEmpty() 
				|| newDevice.getFunctions().size() <= 0) {
			return null;
		}
		
		return newDevice;
	}
	
	
	
	/**
	 * Gets the device id.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @param moduleDescription the module description
	 * @return the device id
	 */
	private String getDeviceId(JsonNode jsonlist2Device, JsonNode moduleDescription) {
		JsonNode mappingDescription = moduleDescription.get("device_id");
		String id = null;
		try {
			String keyPath = mappingDescription.get("key_path").asText();
			JsonNode type = MappingHelper.navigateJsonKeyPath(jsonlist2Device, keyPath);
			id = type.asText();
		} catch (Exception e) {
			log.error("Extracting deviceId failed ("+log_info+")", e);	
		}
		return id;
	}
	
	/**
	 * Gets the type id.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @param moduleDescription the module description
	 * @return the type id
	 */
	private String getTypeId(JsonNode jsonlist2Device, JsonNode moduleDescription) {
		JsonNode mappingDescription = moduleDescription.get("type_id");
		String id = null;
		try {
			String keyPath = mappingDescription.get("key_path").asText();
			JsonNode type = MappingHelper.navigateJsonKeyPath(jsonlist2Device, keyPath);
			id = type.asText().toLowerCase();
		} catch (Exception e) {
			log.error("Extracting typeId failed ("+log_info+")", e);	
		}
		return id;
	}
	
	/**
	 * Gets the room ids.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @param moduleDescription the module description
	 * @return the room ids
	 */
	private List<String> getRoomIds(JsonNode jsonlist2Device, JsonNode moduleDescription) {
		JsonNode mappingDescription = moduleDescription.get("rooms");
		String string;
		try {
			string = MappingHelper.navigateJsonKeyPath(jsonlist2Device, mappingDescription.get("key_path").asText()).asText();
		} catch (NoValidKeyPathException e) {
			string = null;
		}
		return mapRoomIds(string, moduleDescription);
	}
	
	/**
	 * Map room ids.
	 *
	 * @param string the string
	 * @param moduleDescription the module description
	 * @return the list
	 */
	public List<String> mapRoomIds(String string, JsonNode moduleDescription) {
		if (string != null && !string.isEmpty() && moduleDescription.has("rooms")) {
			JsonNode mappingDescription = moduleDescription.get("rooms");
			List<String> list = new ArrayList<>();
			if (mappingDescription.has("delimiters") && !mappingDescription.get("delimiters").asText().isEmpty()) {
				String delimiter = mappingDescription.get("delimiters").asText();
				String[] array = string.split(delimiter);
				Collections.addAll(list, array);
			} else {
				list.add(string);
			}
			
			return list;
		} else {
			return new ArrayList<>();
		}
	}
	
	/**
	 * Gets the group ids.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @param moduleDescription the module description
	 * @return the group ids
	 */
	public List<String> getGroupIds(JsonNode jsonlist2Device, JsonNode moduleDescription) {
		JsonNode mappingDescription = moduleDescription.get("groups");
		String string;
		try {
			string = MappingHelper.navigateJsonKeyPath(jsonlist2Device, mappingDescription.get("key_path").asText()).asText();
		} catch (NoValidKeyPathException e) {
			string = null;
		}
		return mapGroupIds(string, moduleDescription);
		
	}
	
	/**
	 * Map group ids.
	 *
	 * @param string the string
	 * @param moduleDescription the module description
	 * @return the list
	 */
	public List<String> mapGroupIds(String string, JsonNode moduleDescription) {
		if (string != null && !string.isEmpty() && moduleDescription.has("groups")) {
			JsonNode mappingDescription = moduleDescription.get("groups");
			List<String> list = new ArrayList<>();
			if (mappingDescription.has("delimiters") && !mappingDescription.get("delimiters").asText().isEmpty()) {
				String delimiter = mappingDescription.get("delimiters").asText();
				String[] array = string.split(delimiter);
				Collections.addAll(list, array);
			} else {
				list.add(string);
			}
			
			return list;
		} else {
			return new ArrayList<>();
		}
	}

}
