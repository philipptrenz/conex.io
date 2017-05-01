package mapping.set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.FHEMConnector;

public class FHEMCommandBuilder {

	private FHEMConnector connector;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public FHEMCommandBuilder(FHEMConnector connector) {
		
		this.connector = connector;
		
	}
	
	public String buildCommand(List<Device> devices, Function functionValuesToSet) {
		
		/*
		 * TODO
		 * 
		 * check which values of Function to set (could be one or more)
		 * sort devices for each device type
		 * 
		 * for each device type:
		 * 		collect all deviceIds to String, separated by ","
		 *   	parse Function values to String
		 *   	for each value:
		 *   		build string "set "+allDeviceIds+" "+functionValue
		 * concatenate all commands via ";" (?)
		 */
		
		if (functionValuesToSet != null) {
			Map<String, String> deviceIdsByTypeIdMap = concatDeviceIdsByTypeId(devices, functionValuesToSet.getClass());
			
			
			
			Iterator it = deviceIdsByTypeIdMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry) it.next();
		        
		        String typeId = (String) pair.getKey();
		        String concatenatedDeviceIds = (String) pair.getValue();
		        
		        log.info("Setting "+functionValuesToSet.getClass().getSimpleName()+" for "+concatenatedDeviceIds.replace(",", ", ")+" (type_id: "+typeId+")");
		        
		        // TODO: Extract value to set
		        
		        
		        
		        
		        it.remove(); // avoids a ConcurrentModificationException
		    }
			
			return "";
		} else {
			return "";
		}
	}
	
	private Map<String, String> concatDeviceIdsByTypeId(List<Device> deviceList, Class<?> functionClass){
		Map<String, String> deviceIdsByTypeIdMap = new HashMap<>();
		for (Device device : deviceList) {
			boolean containsFunction = false;
			for (Function function : device.getFunctions()) {
				if (function.getClass().equals(functionClass)){
					containsFunction = true;
					break;
				}
			}
			if (containsFunction) {
				String typeId = device.getTypeId();
				
				String concatString = "";
				if (deviceIdsByTypeIdMap.containsKey(typeId)) {
					concatString = deviceIdsByTypeIdMap.get(typeId);
				}
				
				if (!concatString.isEmpty()) {
					// separate by ","
					concatString += ",";
				}
				concatString += device.getDeviceId();
				deviceIdsByTypeIdMap.put(typeId, concatString);
			}
		}
		return deviceIdsByTypeIdMap;
	}
}
