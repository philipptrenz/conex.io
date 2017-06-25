package mapping.get;

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

/**
 * The Class JsonParser.
 * 
 * This class parses the via FHEM command 'jsonlist2'
 * retrieved JSON.
 * 
 * @author Philipp Trenz
 */
public class JsonParser {
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/** The reader. */
	private ObjectReader reader;
	
	/** The mapper. */
	private DeviceMapper mapper;
	
	/** The loader. */
	private ModuleDescriptionLoader loader;
	
	/**
	 * Instantiates a new json parser.
	 */
	public JsonParser() {
		
		this.reader = new ObjectMapper().reader();
		this.loader = new ModuleDescriptionLoader();
		this.mapper = new DeviceMapper(loader);

	}
	
	/**
	 * Parses the.
	 *
	 * @param jsonlist2 the jsonlist 2
	 * @return the array list
	 */
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
