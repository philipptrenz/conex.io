package mapping.get;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class ModuleDescriptionLoader {
	
	private String folderName;
	private ObjectReader reader;
	
	public ModuleDescriptionLoader(String folderName){
		this.folderName = folderName;
		this.reader = new ObjectMapper().reader();
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
	
	/*
	 * This function returns the correct module description for the device from the TYPE-property of jsonlist2
	 */
	public JsonNode getModuleDescription(JsonNode jsonlist2Device) {
		String deviceType = jsonlist2Device.get("Internals").get("TYPE").asText();
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(folderName+"/"+deviceType.toLowerCase()+".json"));
			String moduleDescription = new String(encoded, StandardCharsets.UTF_8);
			return reader.readTree(moduleDescription);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Loading module description '"+deviceType+"' failed: \n"+e.getMessage());
			return null;
		}
	}
	
	public JsonNode getModuleDescription(String deviceType) {
		if (deviceType == null) return null;
		
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(folderName+"/"+deviceType.toLowerCase()+".json"));
			String moduleDescription = new String(encoded, StandardCharsets.UTF_8);
			return reader.readTree(moduleDescription);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
