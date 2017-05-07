package mapping.get;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class ModuleDescriptionLoader {
	
	private String folderName;
	private ObjectReader reader;
	private ClassLoader classLoader;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public ModuleDescriptionLoader(){
		this.folderName = "fhem/descriptions";
		this.reader = new ObjectMapper().reader();
		this.classLoader = getClass().getClassLoader();
	}
	
	/*
	 * This function checks if a FHEM module description exists
	 */
	public boolean moduleDescriptionExists(JsonNode device) {
		String deviceType = device.get("Internals").get("TYPE").asText();
		return moduleDescriptionExists(deviceType);
	}
	
	/*
	 * This function checks if a FHEM module description exists
	 */
	public boolean moduleDescriptionExists(String deviceType) {
		String fileName = deviceType.toLowerCase()+".json";
		try {
			File f = new File(classLoader.getResource(folderName+"/"+fileName).getFile());
			if(f.exists() && !f.isDirectory()) { 
			    return true;
			}
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		
	}
	
	/*
	 * This function returns the correct module description for the device from the TYPE-property of jsonlist2
	 */
	public JsonNode getModuleDescription(JsonNode jsonlist2Device) {
		String deviceType = jsonlist2Device.get("Internals").get("TYPE").asText();
		return getModuleDescription(deviceType);
	}
	
	public JsonNode getModuleDescription(String deviceType) {
		if (deviceType == null) return null;
		String fileName = deviceType.toLowerCase()+".json";
		
		try {
			String moduleDescription = IOUtils.toString(classLoader.getResourceAsStream(folderName+"/"+fileName));
			return reader.readTree(moduleDescription);
		} catch (IOException e) {
			log.error("Loading description file "+folderName+"/"+fileName+" failed", e);
			return null;
		}
	}

}
