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
	
	private List<Device> ausgabe;
	

	/**
	 * Set's the Filter-Object and get the Devices from Mapping layer
	 * @param filter Filter-Object
	 */
	public DeviceCalc(Filter filter) {
        this.filter = filter;
        this.geraete = mapping.Main.getDevices();
	}
	/**
	 * Filtering for all Devices and endpoint /devices. Iterates through all Filter-functions - if required.
	 * @return list of Device's
	 */
	public List<Device> getListFiltering() {
		if(!filter.getDeviceIds().isEmpty() || !filter.getFunctionIds().isEmpty() ||
				!filter.getGroupIds().isEmpty() || !filter.getRoomIds().isEmpty()) {
			for (int i= 0; i < geraete.size(); i++) {
        			Device d = geraete.get(i);
        			if(!filter.getDeviceIds().isEmpty()) {
        				if (!filter.getDeviceIds().contains(d.getDeviceId())) {
        					geraete.remove(d);
        					i--;
        					continue;
        				}
        			}
        			if(!filter.getRoomIds().isEmpty()) {
        				if(Collections.disjoint(filter.getRoomIds(), d.getRoomIds())) {
        					geraete.remove(d);
        					i--;
        					continue;
        				}
        			}
        			if(!filter.getGroupIds().isEmpty()) {
        				if(Collections.disjoint(filter.getGroupIds(), d.getGroupIds())) {
        					geraete.remove(d);
        					i--;
        					continue;
        				}
        			}
        			if(!filter.getFunctionIds().isEmpty()) {
        	    			boolean check = false;
        	        		for(Function func : d.getFunctions()) {
        	        			if (filter.getFunctionIds().contains(func.getFunctionId())) {
        	        					check = true;
        	        				}
        	        			}
        	        			if(!check) {
        	        				geraete.remove(d);
        	    					i--;
        	        					}
        						}
        					}
        				}
        ausgabe = geraete;
		return ausgabe;
	}
	/**
	 * Filtering function for endpoint /functions
	 * @return list of String Ids
	 */
	public List<String> getFuntionFiltering() {
		List<String> functions = new ArrayList<String>();
		for(Device d: getListFiltering()) {
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
	public List<String> getGroupFiltering() {
		List<String> gruppen = new ArrayList<String>();
		for(Device d: getListFiltering()) {
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
	public List<String> getRoomFiltering() {
		List<String> rooms = new ArrayList<String>();
		for(Device d: getListFiltering()) {
			for(String raum: d.getRoomIds()) {
				if(!rooms.contains(raum)) {
					rooms.add(raum);
				}
			}
		}
		return rooms;
	}
}
