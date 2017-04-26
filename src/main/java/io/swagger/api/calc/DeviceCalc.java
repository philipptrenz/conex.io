package io.swagger.api.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.swagger.model.Device;
import io.swagger.model.Filter;
import io.swagger.model.Function;

public class DeviceCalc {

	private Filter filter;
	private List<Device> geraete;
	

	/**
	 * Set's the Filter-Object and get the Devices from Mapping layer
	 * @param filter Filter-Object
	 */
	public DeviceCalc(Filter filter) {
		if(filter != null) {
	        this.filter = filter;
		}
		else {
			this.filter = new Filter();
		}
        this.geraete = mapping.Main.getDevices();
	}
	/**
	 * Filtering for all Devices and endpoint /devices. Iterates through all Filter-functions - if required.
	 * @return list of Device's
	 */
	public List<Device> getDeviceListFiltered() {
		List <Device> ausgabe;
		if(!filter.getDeviceIds().isEmpty() || !filter.getFunctionIds().isEmpty() ||
				!filter.getGroupIds().isEmpty() || !filter.getRoomIds().isEmpty()) {
			for (int i= 0; i < geraete.size(); i++) {
        			Device d = geraete.get(i);
        			if(!isDeviceMatchingFiltering(d, filter)) {
        				geraete.remove(i);
        				i--;
        			}
        		}
        	}
		System.out.println("Geräte sind gefiltert ! " + geraete.size());
        ausgabe = geraete;
		return ausgabe;
	}
	
	public List<Device> getDeviceListFilteringWithPatcherFunction(Function f) {
		if(!filter.getFunctionIds().contains(f.getFunctionId())) {
			filter.addFunctionIdsItem(f.getFunctionId());
		}
		
		List <Device> list = getDeviceListFiltered();
		if(!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				List <Function> functionList = list.get(i).getFunctions();
				boolean check = true;
					for (Function listenFunction: functionList) {
						if(listenFunction.getFunctionId().contains(f.getFunctionId())) {
							check = false;	
					}
				}
					if(check) {
						list.remove(i);
						i--;
					}
			}
		}
		return list;
	}
	/**
	 * Filtering function for endpoint /functions
	 * @return list of String Ids
	 */
	public List<String> getFuntionsByDevicesFiltered() {
		List<String> functions = new ArrayList<String>();
		for(Device d: getDeviceListFiltered()) {
			for(Function f: d.getFunctions()) {
				if(!functions.contains(f.getFunctionId())) {
					functions.add(f.getFunctionId());
				}
			}
		}
		return functions;
	}
	
	/**
	 * Filtering groups for endpoint /groups
	 * @return list of String Ids
	 */
	public List<String> getGroupsByDevicesFiltered() {
		List<String> gruppen = new ArrayList<String>();
		for(Device d: getDeviceListFiltered()) {
			for(String group: d.getGroupIds()) {
				if(!gruppen.contains(group)) {
					gruppen.add(group);
				}
			}
		}
		return gruppen;
	}
	
	/**
	 * Filtering rooms for endpoint /rooms
	 * @return list of String Ids
	 */
	public List<String> getRoomsByDevicesFiltered() {
		List<String> rooms = new ArrayList<String>();
		for(Device d: getDeviceListFiltered()) {
			for(String raum: d.getRoomIds()) {
				if(!rooms.contains(raum)) {
					rooms.add(raum);
				}
			}
		}
		return rooms;
	}
	/**
	 * Checks if a device matching all required filter attributes of the object filter
	 * @param device = The object to be filtered
	 * @param filter = The filter object
	 * @return true if the device matches all required filter attributes. Otherwise return false
	 */
	public static boolean isDeviceMatchingFiltering(Device device, Filter filter) {
		if(!filter.getDeviceIds().isEmpty()) {
			if(!filter.getDeviceIds().contains(device.getDeviceId())) {
				return false;
			}
		}
		if(!filter.getFunctionIds().isEmpty()) {
			boolean matchingFunctions = false;
    		for(Function func : device.getFunctions()) {
    			if (filter.getFunctionIds().contains(func.getFunctionId())) {
    				matchingFunctions = true;
    				}
    			}
    		if(!matchingFunctions) {
    			return false;
    		}
    		}
		if(!filter.getRoomIds().isEmpty()) {
			if(Collections.disjoint(filter.getRoomIds(), device.getRoomIds())) {
				return false;
			}
		}
		if(!filter.getGroupIds().isEmpty()) {
			if(Collections.disjoint(filter.getGroupIds(), device.getGroupIds())) {
				return false;
			}
		}
		return true;
	}
}
