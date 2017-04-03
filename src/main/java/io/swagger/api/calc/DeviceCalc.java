package io.swagger.api.calc;

import java.util.ArrayList;
import java.util.List;

import io.swagger.model.Device;
import io.swagger.model.Filter;

public class DeviceCalc {

	private Filter filter;
	private List<Device> geraete;
	
	private List<Device> ausgabe;
	

	public DeviceCalc(Filter filter) {
        this.filter = filter;
        this.geraete = mapping.Main.getDevices();
        ausgabe = geraete;
        if(!filter.getDeviceIds().isEmpty()) {
        	ausgabe = calcDevices(geraete);
        }
        else if (!filter.getFunctionIds().isEmpty()) {
        	ausgabe = calcFunction(geraete);
        }
        else if (!filter.getGroupIds().isEmpty()) {
        	ausgabe = calcGroup(geraete);
        }
        else if (!filter.getRoomIds().isEmpty()){
        	ausgabe = calcRoom(geraete);
        }
        else {}
	}
	public List<Device> getListFiltering() {
		return ausgabe;
	}
	public List<String> getFuntionFiltering() {
		List<String> functions = new ArrayList<String>();
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
	
	
    private List<Device> calcDevices(List<Device> liste) {
    	while(liste.iterator().hasNext()) {
        	Device d = liste.iterator().next();
			boolean abfrage = true;
    		while(filter.getDeviceIds().iterator().hasNext()) {
    			String deviceId = filter.getDeviceIds().iterator().next();
    			if(d.getDeviceId().contains(deviceId)) {
    				abfrage = false;
    			}
    		}
    		if(abfrage) {
    			liste.remove(d);
    		}
    		//Lösche Device aus Liste
    	}
    	if(liste.isEmpty()) {
    		return liste;
    	}
    	if(!filter.getFunctionIds().isEmpty()) {
    		liste = calcFunction(liste);
    	}
    	else if(!filter.getGroupIds().isEmpty()) {
    		liste = calcGroup(liste);
    	}
    	else if(!filter.getRoomIds().isEmpty()) {
    		liste = calcRoom(liste);
    	}
    	
    	return liste;
    }
    private List<Device> calcFunction(List<Device> liste) {
    	while(liste.iterator().hasNext()) {
        	Device d = liste.iterator().next();
			boolean abfrage = true;
    		while(filter.getFunctionIds().iterator().hasNext()) {
    			String functionId = filter.getFunctionIds().iterator().next();
    			if(d.getFunctions().contains(functionId)) {
    				abfrage = false;
    			
    			}
    		}
    		if(abfrage) {
    			liste.remove(d);
    		}
    		//Lösche Device aus Liste
    	}
    	if(liste.isEmpty()) {
    		return liste;
    	}
    	if(!filter.getGroupIds().isEmpty()) {
    		liste = calcGroup(liste);
    	}
    	else if(!filter.getRoomIds().isEmpty()) {
    		liste = calcRoom(liste);
    	}
    	return liste;
    }
    private List<Device> calcGroup(List<Device> liste) {
    	while(liste.iterator().hasNext()) {
        	Device d = liste.iterator().next();
			boolean abfrage = true;
    		while(filter.getGroupIds().iterator().hasNext()) {
    			String groupId = filter.getGroupIds().iterator().next();
    			if(d.getGroupIds().contains(groupId)) {
    				abfrage = false;
    			
    			}
    		}
    		if(abfrage) {
    			liste.remove(d);
    		}
    		//Lösche Device aus Liste
    	}
    	if(liste.isEmpty()) {
    		return liste;
    	}
    	if(!filter.getRoomIds().isEmpty()) {
    		liste = calcRoom(liste);
    	}
    	return liste;
    }
    private List<Device> calcRoom(List<Device> liste) {
    	while(liste.iterator().hasNext()) {
        	Device d = liste.iterator().next();
			boolean abfrage = true;
    		while(filter.getRoomIds().iterator().hasNext()) {
    			String roomId = filter.getRoomIds().iterator().next();
    			if(d.getRoomIds().contains(roomId)) {
    				abfrage = false;
    			
    			}
    		}
    		if(abfrage) {
    			liste.remove(d);
    		}
    	}
    	return liste;
    }
}
