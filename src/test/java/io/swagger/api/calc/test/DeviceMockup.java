package io.swagger.api.calc.test;

import java.util.ArrayList;
import java.util.List;

import io.swagger.model.Device;
import io.swagger.model.Function;

/**
 * Devices mockup for endpoint testing cases.
 */
public class DeviceMockup {

/**
 * Gets the devices mockup.
 *
 * @return the devices mockup list
 */
public static List<Device> getDevicesMockup() {
		
		List<Device> deviceList = new ArrayList<Device>();
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
			
			
			Function function = new Function();
			function.setFunctionId("testfunction_"+k);
			device.addFunctionsItem(function);
			
			Function function2 = new Function();
			function2.setFunctionId("testfunction_"+(k+1));	
			device.addFunctionsItem(function2);
			
			Function function3 = new Function();
			function3.setFunctionId("testfunction_"+(k+2));
			device.addFunctionsItem(function3);
			
			deviceList.add(device);
			
			j++;
			if (j > 10) j = 0;
			if (k > 15) k = 0;
		}
		return deviceList;
		
	}

}
