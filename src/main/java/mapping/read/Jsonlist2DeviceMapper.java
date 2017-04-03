package mapping.read;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.exceptions.MalformedFHEMModuleDescriptionJsonException;

/*
 * Class to load FHEM module descriptions
 * 
 * TODO: Add caching for reduced IO-accesses
 */
public class Jsonlist2DeviceMapper {
	
	private ObjectReader reader;
	private String folderName;
	private ObjectMapper mapper;
	private FunctionMappingRequirementsValidator requirementsValidator;
	private MappingValueExtractor extractor;
	
	/*
	 * Constructor
	 */
	public Jsonlist2DeviceMapper(String folderName) {
		this.folderName = folderName;
		this.reader = new ObjectMapper().reader();
		this.mapper = new ObjectMapper();
		this.requirementsValidator = new FunctionMappingRequirementsValidator();
		this.extractor = new MappingValueExtractor();
	}
	
	/*
	 * 
	 */
	public Device mapJsonlist2Device(JsonNode jsonlist2Device)  {
		Device newDevice = new Device();
		
		String deviceName = jsonlist2Device.get("Name").asText();
		System.out.println("Mapping device '"+deviceName+"'");
		
		newDevice.setDeviceId(deviceName);
		

		try {
			JsonNode moduleDescription = getModuleDescription(jsonlist2Device);
			
			// choose read part for FHEM to Java Mapping
			JsonNode readDescription = moduleDescription.get("read");
			Iterator<Map.Entry<String, JsonNode>> fields = readDescription.fields();
			
			// iterate over all available functions defined in the module description
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> entry = fields.next();
				//System.out.println("Mapping function "+entry.getKey()+" ...");
				
				try {
					Function mappedFunction = mapFunction(entry.getKey(), entry.getValue(), jsonlist2Device);
					if (mappedFunction != null) newDevice.addFunctionsItem(mappedFunction);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedFHEMModuleDescriptionJsonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newDevice;
	}
	
	/*
	 * This function maps values from jsonlist2 to conex.io Functions
	 */
	private Function mapFunction(String functionName, JsonNode mappingDescription, JsonNode jsonlist2Device) throws ClassNotFoundException, InstantiationException, IllegalAccessException, JsonProcessingException, MalformedFHEMModuleDescriptionJsonException  {
		
		// 0. Check if jsonlist2 data fits requirements
		if (requirementsValidator.doFitRequirements(mappingDescription.get("requirements"), jsonlist2Device)) {
			
			// 1. Generate prototype JSON as JsonNode from Java Class by classname
			String className = mappingDescription.get("classname").asText();
			Class<?> functionClass = Class.forName(className);
			Object function = functionClass.newInstance();
			ObjectNode proto = mapper.valueToTree(function);
			
			
			// 2. Insert values from jsonlist2 to prototype
			
			// iterate over all properties to get the values mapped
			Iterator<Map.Entry<String, JsonNode>> fields;
			try {
				fields = mappingDescription.get("properties").fields();
				while (fields.hasNext()) {
					Map.Entry<String, JsonNode> entry = fields.next();
					String key = entry.getKey();
					JsonNode property = entry.getValue();
					
					String value = extractor.extractValue(jsonlist2Device, property, key, function);
					proto.put(key, value);
				}
			} catch (NullPointerException e) {
				throw new MalformedFHEMModuleDescriptionJsonException(functionName, folderName);
			}
			
			// 3. Map back to Java object
			
			function = mapper.treeToValue(proto, Class.forName(className));
			
			System.out.println(function);
			
			return (Function) function;
		
		} else {
			return null;
		}
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
	private JsonNode getModuleDescription(JsonNode jsonlist2Device) throws IOException {
		String deviceType = jsonlist2Device.get("Internals").get("TYPE").asText();
		byte[] encoded = Files.readAllBytes(Paths.get(folderName+"/"+deviceType.toLowerCase()+".json"));
		
		String moduleDescription = new String(encoded, StandardCharsets.UTF_8);
		return reader.readTree(moduleDescription);
	}

}
