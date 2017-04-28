package mapping.set;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrlPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ch.qos.logback.core.joran.action.IADataForComplexProperty;
import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.FHEMConnector;

public class FHEMCommandBuilder {

	private FHEMConnector connector;
	
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
		        
		        //System.out.println(typeId+": "+concatenatedDeviceIds);
		        
		        // TODO: Extract value to set
		        
		        
		        
		        
		        it.remove(); // avoids a ConcurrentModificationException
		    }
			
			return "";
		} else {
			return "";
		}
		
	}
	
	private Map<String, String> concatDeviceIdsByTypeId(List<Device> deviceList, Class<?> functionToSetClass){
		Map<String, String> deviceIdsByTypeIdMap = new HashMap<>();
		for (Device device : deviceList) {
			boolean containsFunction = false;
			for (Function function : device.getFunctions()) {
				if (function.getClass().equals(functionToSetClass)){
					containsFunction = true;
					break;
				}
			}
			if (containsFunction) {
				String typeId = device.getTypeId();
				if (!deviceIdsByTypeIdMap.containsKey(typeId)) {
					// instantiate
					deviceIdsByTypeIdMap.put(typeId, "");
				}
				if (!deviceIdsByTypeIdMap.get(typeId).isEmpty()) {
					// separate by ","
					deviceIdsByTypeIdMap.get(typeId).concat(",");
				}
				deviceIdsByTypeIdMap.get(typeId).concat(device.getDeviceId());
			}
		}
		return deviceIdsByTypeIdMap;
	}
	
}
