package mapping.get;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.swagger.model.Device;
import mapping.FHEMConnector;

public class JsonParser {
	
	private ObjectReader reader;
	private DeviceMapper mapper;
	private ModuleDescriptionLoader loader;
	
	public JsonParser() {
		
		this.reader = new ObjectMapper().reader();
		this.loader = new ModuleDescriptionLoader("module_descriptions");
		this.mapper = new DeviceMapper(loader);

	}
	
	public ArrayList<Device> parse() {
		
		ArrayList<Device> deviceList = new ArrayList<>();
		
		try {
			
			String jsonlist2 = FHEMConnector.getJsonlist2Result();
			
			JsonNode root = reader.readTree(jsonlist2);
			ArrayNode jsonlist2Devices = (ArrayNode) root.get("Results");
			Iterator<JsonNode> devicesIterator = jsonlist2Devices.elements();
			
			int index = 0;
			
			while (devicesIterator.hasNext()) {
				
				JsonNode jsonDevice = devicesIterator.next();		
				
				if (loader.moduleDescriptionExists(jsonDevice)) {
					//System.out.println(jsonDevice.get("Name")+": module description exists; trying to map ...");
					
					try {
						Device device = mapper.mapJsonToDevice(jsonDevice);
						if (device != null) {
							//System.out.println("device added");
							deviceList.add(device);
							
							System.out.println(device+"\n");
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					//System.out.println(jsonDevice.get("Name")+" ignored \n");
				}
				index++;
			}
			
			System.out.println("# parsed devices: "+index);
				
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return deviceList;
	}
	
	
}
