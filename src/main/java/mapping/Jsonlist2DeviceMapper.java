package mapping;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.model.Device;

/*
 * Class to load FHEM module descriptions
 * 
 * TODO: Add caching for reduced IO-accesses
 */
public class Jsonlist2DeviceMapper {
	
	private String folderName;
	
	/*
	 * Constructor
	 */
	public Jsonlist2DeviceMapper(String folderName) {
		this.folderName = folderName;
	}
	
	/*
	 * 
	 */
	public Device mapJsonlist2Device(JsonNode jsonlist2Device) throws Exception {
		
		// TODO: Implement all the mapping stuff
		
		return new Device();
	}
	
	
	/*
	 * This function checks if a FHEM module description exists
	 */
	public boolean moduleDescriptionExists(JsonNode device) {
		String deviceType = device.get("Internals").get("TYPE").asText();
		
		File f = new File(folderName+"/"+deviceType.toLowerCase()+".json");
		if(f.exists() && !f.isDirectory()) { 
		    return true;
		}
		return false;
	}
}
