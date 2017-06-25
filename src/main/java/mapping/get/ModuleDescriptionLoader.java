package mapping.get;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * The Class ModuleDescriptionLoader.
 * 
 * This class provides the functionality to load module
 * descriptions from file IO.
 * 
 * TODO: Cache module descriptions to reduce file access.
 * 
 * @author Philipp Trenz
 */
public class ModuleDescriptionLoader {
	
	/** The folder name. */
	private String folderName;
	
	/** The reader. */
	private ObjectReader reader;
	
	/** The class loader. */
	private ClassLoader classLoader;
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Instantiates a new module description loader.
	 */
	public ModuleDescriptionLoader(){
		this.folderName = "fhem/descriptions";
		this.reader = new ObjectMapper().reader();
		this.classLoader = getClass().getClassLoader();
	}
	
	/**
	 * Module description exists.
	 *
	 * @param device the device
	 * @return true, if successful
	 */
	/*
	 * This function checks if a FHEM module description exists
	 */
	public boolean moduleDescriptionExists(JsonNode device) {
		String deviceType = device.get("Internals").get("TYPE").asText();
		return moduleDescriptionExists(deviceType);
	}
	
	/**
	 * Module description exists.
	 *
	 * @param deviceType the device type
	 * @return true, if successful
	 */
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
	
	/**
	 * Gets the module description.
	 *
	 * @param jsonlist2Device the jsonlist 2 device
	 * @return the module description
	 */
	/*
	 * This function returns the correct module description for the device from the TYPE-property of jsonlist2
	 */
	public JsonNode getModuleDescription(JsonNode jsonlist2Device) {
		String deviceType = jsonlist2Device.get("Internals").get("TYPE").asText();
		return getModuleDescription(deviceType);
	}
	
	/**
	 * Gets the module description.
	 *
	 * @param deviceType the device type
	 * @return the module description
	 */
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
