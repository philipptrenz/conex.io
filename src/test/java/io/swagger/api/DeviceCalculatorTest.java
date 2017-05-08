package io.swagger.api;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.*;

import io.swagger.api.calc.DeviceCalcTestCase;
import io.swagger.model.Device;
import io.swagger.model.Filter;
import io.swagger.model.Function;

public class DeviceCalculatorTest {
	
	/**
	 * Devices
	 */
	
	@Test
	public void getDevicesByAllFilters() {
    	List <String> searchDevices = Arrays.asList("testdevice_0");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	for(Device d : list) {
    		if((f.getDeviceIds().contains(d.getDeviceId())) && 
    				(!Collections.disjoint(f.getGroupIds(), d.getGroupIds())) && 
    				(!Collections.disjoint(f.getRoomIds(), d.getRoomIds()))) {
    			boolean functionCheck = false;
    			for (Function function : d.getFunctions()) {
    				if(f.getFunctionIds().contains(function.getFunctionId())) {
    					functionCheck = true;
    				}
    			}
    			assertTrue(functionCheck);
    		}
    		else {
    			assertTrue(false);
    		}
    	}
	}
	@Test
	public void getDevicesByDeviceOnly() {
		List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	for(Device d : list) {
    		assertTrue(f.getDeviceIds().contains(d.getDeviceId()));
    	}
	}
	@Test
	public void getDevicesByFunctionOnly() {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchFunctions);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	for(Device d : list) {
    			boolean functionCheck = false;
    			for (Function function : d.getFunctions()) {
    				if(f.getFunctionIds().contains(function.getFunctionId())) {
    					functionCheck = true;
    				}
    			}
    			assertTrue(functionCheck);
    	}
	}
	@Test
	public void getDevicesByGroupOnly() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	for(Device d : list) {
    		assertTrue(!Collections.disjoint(f.getGroupIds(), d.getGroupIds()));
    	}
	}
	@Test
	public void getDevicesByRoomOnly() {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	for(Device d : list) {
    		assertTrue(!Collections.disjoint(f.getRoomIds(), d.getRoomIds()));
    	}
	}
	@Test
	public void getDevicesByMultipleDifferentFilters() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	for(Device d : list) {
    		assertTrue(!Collections.disjoint(f.getRoomIds(), d.getRoomIds()) && !Collections.disjoint(f.getGroupIds(), d.getGroupIds()));
    	}
	}
	@Test
	public void getAllDevicesByNoneFiltering() {
		DeviceCalcTestCase dc = new DeviceCalcTestCase(new Filter());
		List <Device> list = dc.getDeviceListFiltered();
		
		assertTrue(list.size() == io.swagger.api.calc.DeviceMockup.getDevicesMockup().size());
	}
	@Test
	public void getNoDevices() {
		List <String> searchDevices = Arrays.asList("non_existing_device");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	assertTrue(list.isEmpty());
	}
	@Test
	public void deviceIdsAsNone() {
		List <String> searchDevices = null;    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <Device> list = dc.getDeviceListFiltered();
    	assertEquals(list, io.swagger.api.calc.DeviceMockup.getDevicesMockup());		
	}

	
	/**
	 * Functions
	 */
	@Test
	public void getFunctionsByAllFilters() {
    	List <String> searchDevices = Arrays.asList("testdevice_0");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	assertTrue(!Collections.disjoint(list, searchFunctions));
	}
	@Test
	public void getFunctionsByDeviceOnly() {
		List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	
    	assertTrue(list.contains("testfunction_0"));
    	assertTrue(list.contains("testfunction_1"));
	}
	@Test
	public void getFunctionsByFunctionOnly() {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchFunctions);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchFunctions, list));
	}
	@Test
	public void getFucntionsByGroupOnly() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	
    	assertTrue(list.contains("testfunction_0"));
    	assertTrue(list.contains("testfunction_1"));
	}
	@Test
	public void getFunctionsByRoomOnly() {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	
    	assertTrue(list.contains("testfunction_0"));
    	assertTrue(list.contains("testfunction_1"));
	}
	@Test
	public void getFunctionsByMultipleDifferentFilters() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	
    	assertTrue(list.contains("testfunction_0"));
    	assertTrue(list.contains("testfunction_1"));
	}
	@Test
	public void getAllFunctionsByNoneFiltering() {
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(new Filter());
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	
    	assertTrue(list.contains("testfunction_0"));
    	assertTrue(list.contains("testfunction_1"));
    	assertTrue(list.contains("testfunction_2"));
	}
	@Test
	public void getNoFunctions() {
		List <String> searchDevices = Arrays.asList("non_existing_device");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	assertTrue(list.isEmpty());
	}
	@Test
	public void functionIdsAsNone() {
		List <String> searchFunction = null;    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchFunction);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getFuntionsByDevicesFiltered();
    	assertTrue(!list.isEmpty());		
	}

	
	
	/**
	 * Groups
	 */
	@Test
	public void getGroupsByAllFilters() {
    	List <String> searchDevices = Arrays.asList("testdevice_0");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	assertTrue(!Collections.disjoint(list, searchGroups));
	}
	@Test
	public void getGroupsByDeviceOnly() {
		List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	
    	assertTrue(list.contains("testgroup_0"));
    	assertTrue(list.contains("testgroup_1"));
	}
	@Test
	public void getGroupsByFunctionOnly() {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchFunctions);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	
    	assertTrue(list.contains("testgroup_0"));
	}
	@Test
	public void getGroupsByGroupOnly() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchGroups, list));
	}
	@Test
	public void getGroupsByRoomOnly() {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	
    	assertTrue(list.contains("testgroup_0"));
    	assertTrue(list.contains("testgroup_1"));
	}
	@Test
	public void getGroupsByMultipleDifferentFilters() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchGroups, list));
	}
	@Test
	public void getAllGroupsByNoneFiltering() {
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(new Filter());
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	
    	assertTrue(list.contains("testgroup_0"));
    	assertTrue(list.contains("testgroup_1"));
    	assertTrue(list.contains("testgroup_2"));
	}
	@Test
	public void getNoGroups() {
		List <String> searchDevices = Arrays.asList("non_existing_device");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	assertTrue(list.isEmpty());
	}
	@Test
	public void groupIdsAsNone() {
		List <String> searchGroup = null;    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchGroup);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getGroupsByDevicesFiltered();
    	assertTrue(!list.isEmpty());		
	}
	
	
	/**
	 * Rooms
	 */
	@Test
	public void getRoomsByAllFilters() {
    	List <String> searchDevices = Arrays.asList("testdevice_0");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	assertTrue(!Collections.disjoint(list, searchRooms));
	}
	@Test
	public void getRoomsByDeviceOnly() {
		List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	
    	assertTrue(list.contains("testroom_0"));
    	assertTrue(list.contains("testroom_1"));
	}
	@Test
	public void getRoomsByFunctionOnly() {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchFunctions);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	
    	assertTrue(list.contains("testroom_0"));
	}
	@Test
	public void getRoomsByGroupOnly() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	
    	assertTrue(list.contains("testroom_0"));
    	assertTrue(list.contains("testroom_1"));
	}
	@Test
	public void getRoomsByRoomOnly() {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchRooms, list));
	}
	@Test
	public void getRoomsByMultipleDifferentFilters() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchRooms, list));
	}
	@Test
	public void getAllRoomsByNoneFiltering() {
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(new Filter());
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	
    	assertTrue(list.contains("testroom_0"));
    	assertTrue(list.contains("testroom_1"));
    	assertTrue(list.contains("testroom_2"));
	}
	@Test
	public void getNoRooms() {
		List <String> searchDevices = Arrays.asList("non_existing_device");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	assertTrue(list.isEmpty());
	}
	@Test
	public void roomIdsAsNone() {
		List <String> searchRoom = null;    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchRoom);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(f);
    	List <String> list = dc.getRoomsByDevicesFiltered();
    	assertTrue(!list.isEmpty());		
	}
}