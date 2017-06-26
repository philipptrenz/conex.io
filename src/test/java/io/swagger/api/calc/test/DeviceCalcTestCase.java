package io.swagger.api.calc.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.swagger.model.Device;
import io.swagger.model.Filter;
import io.swagger.model.Function;

public class DeviceCalcTestCase {
	private Filter filter;
	private List<Device> allDevicesList;
	

	/**
	 * Set's the Filter-Object and get the Devices from Mapping layer
	 * @param filter Filter-Object
	 */
	public DeviceCalcTestCase(Filter filter) {
		if(filter != null) {
	        this.filter = filter;
		}
		else {
			this.filter = new Filter();
		}
        this.allDevicesList = io.swagger.api.calc.test.DeviceMockup.getDevicesMockup();
	}
	/**
	 * Filtering for all Devices and endpoint /devices. Iterates through all Filter-functions - if required.
	 * @return list of Device's
	 */
	public List<Device> getDeviceListFiltered() {
		List <Device> filteredDevicesResult;
		if(!filter.getDeviceIds().isEmpty() || !filter.getFunctionIds().isEmpty() ||
				!filter.getGroupIds().isEmpty() || !filter.getRoomIds().isEmpty()) {
			for (int filteringDevicesCounter= 0; filteringDevicesCounter < allDevicesList.size(); filteringDevicesCounter++) {
        			Device device = allDevicesList.get(filteringDevicesCounter);
        			if(!isDeviceMatchingFiltering(device, filter)) {
        				allDevicesList.remove(filteringDevicesCounter);
        				filteringDevicesCounter--;
        			}
        		}
        	}
        filteredDevicesResult = allDevicesList;
		return filteredDevicesResult;
	}
	
	public List<Device> getDeviceListFilteringWithPatcherFunction(Function function) {
		if(function == null || function.getFunctionId() == null) {
			function = new Function();
			function.setFunctionId("null");
		}
		if(!filter.getFunctionIds().contains(function.getFunctionId())) {
			filter.addFunctionIdsItem(function.getFunctionId());
		}
		
		List <Device> filteredDevicesList = getDeviceListFiltered();
		if(!filteredDevicesList.isEmpty()) {
			for (int devicePatcherFunctionCounter = 0; devicePatcherFunctionCounter < filteredDevicesList.size(); devicePatcherFunctionCounter++) {
				List <Function> functionList = filteredDevicesList.get(devicePatcherFunctionCounter).getFunctions();
				boolean deviceFuntionsContainsSpecificFunction = true;
					for (Function listenFunction: functionList) {
						if(listenFunction.getFunctionId().contains(function.getFunctionId())) {
							deviceFuntionsContainsSpecificFunction = false;	
					}
				}
					if(deviceFuntionsContainsSpecificFunction) {
						filteredDevicesList.remove(devicePatcherFunctionCounter);
						devicePatcherFunctionCounter--;
					}
				}
			}
		return filteredDevicesList;
	}
	/**
	 * Filtering function for endpoint /functions
	 * @return list of String Ids
	 */
	public List<String> getFuntionsByDevicesFiltered() {
		List<String> filteredFunctions = new ArrayList<String>();
		for(Device device: getDeviceListFiltered()) {
			for(Function f: device.getFunctions()) {
				if(!filteredFunctions.contains(f.getFunctionId())) {
					filteredFunctions.add(f.getFunctionId());
				}
			}
		}
		return filteredFunctions;
	}
	
	/**
	 * Filtering groups for endpoint /groups
	 * @return list of String Ids
	 */
	public List<String> getGroupsByDevicesFiltered() {
		List<String> filteredGroups = new ArrayList<String>();
		for(Device device: getDeviceListFiltered()) {
			for(String group: device.getGroupIds()) {
				if(!filteredGroups.contains(group)) {
					filteredGroups.add(group);
				}
			}
		}
		return filteredGroups;
	}
	
	/**
	 * Filtering rooms for endpoint /rooms
	 * @return list of String Ids
	 */
	public List<String> getRoomsByDevicesFiltered() {
		List<String> filteredRooms = new ArrayList<String>();
		for(Device device: getDeviceListFiltered()) {
			for(String room: device.getRoomIds()) {
				if(!filteredRooms.contains(room)) {
					filteredRooms.add(room);
				}
			}
		}
		return filteredRooms;
	}
	/**
	 * Checks if a device matching all required filter attributes of the object filter
	 * @param device = The object to be filtered
	 * @param filter = The filter object
	 * @return true if the device matches all required filter attributes. Otherwise return false
	 */
	public static boolean isDeviceMatchingFiltering(Device device, Filter filter) {
		if(filter.getDeviceIds() != null && !filter.getDeviceIds().isEmpty()) {
			if(!filter.getDeviceIds().contains(device.getDeviceId())) {
				return false;
			}
		}
		if(filter.getFunctionIds() != null && !filter.getFunctionIds().isEmpty()) {
			boolean deviceFuntionsContainsSpecificFunction = false;
    		for(Function func : device.getFunctions()) {
    			if (filter.getFunctionIds().contains(func.getFunctionId())) {
    				deviceFuntionsContainsSpecificFunction = true;
    				}
    			}
    		if(!deviceFuntionsContainsSpecificFunction) {
    			return false;
    		}
    		}
		if(filter.getRoomIds() != null && !filter.getRoomIds().isEmpty()) {
			if(Collections.disjoint(filter.getRoomIds(), device.getRoomIds())) {
				return false;
			}
		}
		if(filter.getGroupIds() != null && !filter.getGroupIds().isEmpty()) {
			if(Collections.disjoint(filter.getGroupIds(), device.getGroupIds())) {
				return false;
			}
		}
		return true;
	}
}
