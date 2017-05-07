package mapping.get;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.swagger.model.Device;

public class JsonParser {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ObjectReader reader;
	private DeviceMapper mapper;
	private ModuleDescriptionLoader loader;
	
	public JsonParser() {
		
		this.reader = new ObjectMapper().reader();
		this.loader = new ModuleDescriptionLoader();
		this.mapper = new DeviceMapper(loader);

	}
	
	public ArrayList<Device> parse(String jsonlist2) {
		
		ArrayList<Device> deviceList = new ArrayList<>();
		try {			
			JsonNode root = reader.readTree(jsonlist2);
			ArrayNode jsonlist2Devices = (ArrayNode) root.get("Results");
			Iterator<JsonNode> devicesIterator = jsonlist2Devices.elements();
			
			while (devicesIterator.hasNext()) {
				
				JsonNode jsonDevice = devicesIterator.next();		
				
				if (loader.moduleDescriptionExists(jsonDevice)) {					
					try {
						Device device = mapper.mapJsonToDevice(jsonDevice);
						if (device != null) {
							deviceList.add(device);
						}
						
					} catch (Exception e) {
						log.error("An error occured while mapping json to Device", e);
					}
				}
			}
				
		} catch (JsonProcessingException e) {
			log.error("While parsing json an error occured", e);
		} catch (IOException e) {
			log.error("While parsing json an error occured", e);
		}
		return deviceList;
	}
}
