package io.swagger.api.calc;

import java.util.ArrayList;
import java.util.List;

import io.swagger.model.Device;
import io.swagger.model.Filter;

public class DeviceCalc {

	private Filter filter;
	private List<Device> geraete;
	
	private List<Device> ausgabe;
	

	/*
	 * als HashMap ? Array in HashMap // oder Liste !
	 * pro Device -> eine Function 
	 */
	public DeviceCalc(Filter filter) {
        this.filter = filter;
        this.geraete = mapping.Main.getDevices();
	}
	public List<Device> getListFiltering() {
        if(!filter.getDeviceIds().isEmpty() && !geraete.isEmpty()) {
        	while(geraete.iterator().hasNext()) {
            	Device d = geraete.iterator().next();
    			boolean abfrage = true;
        		while(filter.getDeviceIds().iterator().hasNext()) {
        			String deviceId = filter.getDeviceIds().iterator().next();
        			if(d.getDeviceId().contains(deviceId)) {
        				abfrage = false;
        			}
        		}
        		if(abfrage) {
        			geraete.remove(d);
        		}
        	}
        }
        else if (!filter.getFunctionIds().isEmpty() && !geraete.isEmpty()) {
        	while(geraete.iterator().hasNext()) {
            	Device d = geraete.iterator().next();
    			boolean abfrage = true;
        		while(filter.getFunctionIds().iterator().hasNext()) {
        			String functionId = filter.getFunctionIds().iterator().next();
        			if(d.getFunctions().contains(functionId)) {
        				abfrage = false;
        			
        			}
        		}
        		if(abfrage) {
        			geraete.remove(d);
        		}
        	}
        }
        else if (!filter.getGroupIds().isEmpty() && !geraete.isEmpty()) {
        	while(geraete.iterator().hasNext()) {
            	Device d = geraete.iterator().next();
    			boolean abfrage = true;
        		while(filter.getGroupIds().iterator().hasNext()) {
        			String groupId = filter.getGroupIds().iterator().next();
        			if(d.getGroupIds().contains(groupId)) {
        				abfrage = false;
        			
        			}
        		}
        		if(abfrage) {
        			geraete.remove(d);
        		}
        	}
        }
        else if (!filter.getRoomIds().isEmpty() && !geraete.isEmpty()){
        	while(geraete.iterator().hasNext()) {
            	Device d = geraete.iterator().next();
    			boolean abfrage = true;
        		while(filter.getRoomIds().iterator().hasNext()) {
        			String roomId = filter.getRoomIds().iterator().next();
        			if(d.getRoomIds().contains(roomId)) {
        				abfrage = false;
        			
        			}
        		}
        		if(abfrage) {
        			geraete.remove(d);
        		}
        	}
        }
        else {}
        ausgabe = geraete;
		return ausgabe;
	}
	public List<String> getFuntionFiltering() {
		List<String> functions = new ArrayList<String>();
		getListFiltering();
		while(ausgabe.iterator().hasNext()) {
			Device d = ausgabe.iterator().next();
			while(d.getFunctions().iterator().hasNext()) {
				String func = d.getFunctions().iterator().next().getFunctionId();
				if(!functions.contains(func)) {
					functions.add(func);
				}
			}
		}
		return functions;
	}
	
	public List<String> getGroupFiltering() {
		List<String> gruppen = new ArrayList<String>();
		getListFiltering();
		while(ausgabe.iterator().hasNext()) {
			Device d = ausgabe.iterator().next();
			while(d.getGroupIds().iterator().hasNext()) {
				String func = d.getGroupIds().iterator().next();
				if(!gruppen.contains(func)) {
					gruppen.add(func);
				}
			}
		}
		return gruppen;
	}
	
	public List<String> getRoomFiltering() {
		List<String> rooms = new ArrayList<String>();
		getListFiltering();
		while(ausgabe.iterator().hasNext()) {
			Device d = ausgabe.iterator().next();
			while(d.getRoomIds().iterator().hasNext()) {
				String func = d.getRoomIds().iterator().next();
				if(!rooms.contains(func)) {
					rooms.add(func);
				}
			}
		}
		return rooms;
	}
}
