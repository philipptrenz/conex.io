package mapping;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.get.JsonParser;

public class Main {
	
	
	/*
	 * For testing purposes
	 */
	public static void main(String [] args){
		
		/*
		for (Device device : mapping.Main.getDevices()) {
			System.out.println(device+"\n");
		}*/
		
		System.out.println();
		
		JsonParser parser = new JsonParser();
		List<Device> devices = parser.parse();
		
		System.out.println("# mapped devices: "+devices.size());
		
		/*
		 *  TODO: 
		 *  
		 *  1. Sort out with filter object
		 *  2. return devices or retrieve ids (function, room, group) and return
		 */
	}
	
	public static List<Device> getDevices() {
		List<Device> list = new ArrayList<Device>();
		
		int j = 0;
		int k = 0;
		for (int i = 0; i < 50; i++) {
			
			Device device = new Device();
			device.setDeviceId("testdevice_"+i);
			
			device.addGroupIdsItem("testgroup_"+j);
			device.addGroupIdsItem("testgroup_"+(j+1));
			device.addGroupIdsItem("testgroup_"+(j+2));
			
			device.addRoomIdsItem("testroom_"+j);
			device.addRoomIdsItem("testroom_"+(j+1));
			device.addRoomIdsItem("testroom_"+(j+2));
			
			
			Function f = new Function();
			f.setFunctionId("testfunction_"+k);
			device.addFunctionsItem(f);
			
			Function f2 = new Function();
			f2.setFunctionId("testfunction_"+(k+1));	
			device.addFunctionsItem(f2);
			
			Function f3 = new Function();
			f3.setFunctionId("testfunction_"+(k+2));
			device.addFunctionsItem(f3);
			
			
			
			list.add(device);
			
			j++;
			if (j > 10) j = 0;
			if (k > 15) k = 0;
		}
		return list;
	}

}
