package io.swagger.api;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.*;

import io.swagger.api.calc.DeviceCalc;
import io.swagger.model.Device;
import io.swagger.model.Filter;

public class DeviceCalculatorTest {
	
	/**
	 * Devices
	 */
	
	@Test
	public void getDevicesByAllFilters() {
    	List <String> searchDevices = Arrays.asList("dg.jz.deckenleuchte");
    	List <String> searchFunctions = Arrays.asList("onoff");
    	List <String> searchGroups = Arrays.asList("Schalter");
    	List <String> searchRooms = Arrays.asList("DG.Jolina");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalc dc = new DeviceCalc(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	for(Device d : list) {
    		
    	}
	}
	@Test
	public void getDevicesByDeviceOnly() {
		//TODO add method logic
	}
	@Test
	public void getDevicesByFunctionOnly() {
		//TODO add method logic
	}
	@Test
	public void getDevicesByGroupOnly() {
		//TODO add method logic
	}
	@Test
	public void getDevicesByRoomOnly() {
		//TODO add method logic
	}
	@Test
	public void getDevicesByMultipleDifferentFilters() {
		//TODO add method logic
	}
	@Test
	public void getAllDevicesByNoneFiltering() {
		//TODO add method logic
	}
	@Test
	public void getNoDevices() {
		//TODO add method logic
	}

	
	/**
	 * Functions
	 */
	//TODO copy devices -> functions
	
	
	/**
	 * Groups
	 */
	//TODO copy devices -> groups
	
	
	/**
	 * Rooms
	 */
	//TODO copy devices -> rooms
}
