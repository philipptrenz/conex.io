package mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import io.swagger.model.Device;
import io.swagger.model.Devices;
import io.swagger.model.Function;
import mapping.get.JsonParser;

public class Main {
	
	private static FHEMConnector connector;
	
	/*
	 * For testing purposes
	 */
	public static void main(String [] args){
		
		connector = new FHEMConnector("192.168.0.11", 8083);
		
	}
	
	public static List<Device> getDevices() {
		
		return null;
	}

}
