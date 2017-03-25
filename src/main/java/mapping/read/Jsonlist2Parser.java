package mapping.read;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.swagger.model.Device;
import mapping.FHEMJsonlist2Connector;

public class Jsonlist2Parser {
	
	ObjectReader reader;
	Jsonlist2DeviceMapper mapper;
	
	public Jsonlist2Parser() {
		
		this.reader = new ObjectMapper().reader();
		this.mapper = new Jsonlist2DeviceMapper("module_descriptions");

	}
	
	public ArrayList<Device> parse() {
		
		ArrayList<Device> deviceList = new ArrayList<>();
		
		try {
			
			String jsonlist2 = FHEMJsonlist2Connector.getJsonlist2Result();
			
			JsonNode root = reader.readTree(jsonlist2);
			ArrayNode jsonlist2Devices = (ArrayNode) root.get("Results");
			Iterator<JsonNode> devicesIterator = jsonlist2Devices.elements();
			
			int index = 0;
			
			while (devicesIterator.hasNext()) {
				
				JsonNode jsonDevice = devicesIterator.next();		
				
				if (mapper.moduleDescriptionExists(jsonDevice)) {
					System.out.println(jsonDevice.get("Name")+": module description exists; trying to map ...");
					
					try {
						Device device = mapper.mapJsonlist2Device(jsonDevice);
						deviceList.add(device);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println();
				} else {
					//System.out.println(jsonDevice.get("Name")+" ignored \n");
				}
				index++;
			}
			
			System.out.println("# devices: "+index);
				
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
