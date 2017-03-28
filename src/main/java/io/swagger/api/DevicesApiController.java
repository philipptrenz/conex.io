package io.swagger.api;

import io.swagger.model.Device;
import io.swagger.model.Devices;
import io.swagger.model.Filter;
import io.swagger.model.Function;
import io.swagger.model.Patcher;

import io.swagger.annotations.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Controller
public class DevicesApiController implements DevicesApi {
	private Filter filter;
	private List<Device> geraete;

    public ResponseEntity<Void> devicesPatch(@ApiParam(value = "Filter object with function values" ,required=true ) @RequestBody Patcher patcher) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Devices> devicesPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter) {
        this.filter = filter;
        this.geraete = mapping.Main.getDevices();
        List<Device> ergebnis = geraete;
        
        if(!filter.getDeviceIds().isEmpty()) {
        	ergebnis = calcDevices(geraete);
        }
        else if (!filter.getFunctionIds().isEmpty()) {
        	ergebnis = calcFunction(geraete);
        }
        else if (!filter.getGroupIds().isEmpty()) {
        	ergebnis = calcGroup(geraete);
        }
        else if (!filter.getRoomIds().isEmpty()){
        	ergebnis = calcRoom(geraete);
        }
        else {
        	//return geraete;
        }
        //return ergebnis
        return new ResponseEntity<Devices>(HttpStatus.OK);
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
