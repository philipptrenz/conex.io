package io.swagger.api;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.*;

import io.swagger.calc.test.DeviceCalcTestCase;
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
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	filter.setFunctionIds(searchFunctions);
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase dc = new DeviceCalcTestCase(filter);
    	List <Device> filteredDeviceList = dc.getDeviceListFiltered();
    	for(Device device : filteredDeviceList) {
    		if((filter.getDeviceIds().contains(device.getDeviceId())) && 
    				(!Collections.disjoint(filter.getGroupIds(), device.getGroupIds())) && 
    				(!Collections.disjoint(filter.getRoomIds(), device.getRoomIds()))) {
    			boolean deviceFuntionsContainsSpecificFuntion = false;
    			for (Function function : device.getFunctions()) {
    				if(filter.getFunctionIds().contains(function.getFunctionId())) {
    					deviceFuntionsContainsSpecificFuntion = true;
    				}
    			}
    			assertTrue(deviceFuntionsContainsSpecificFuntion);
    		}
    		else {
    			assertTrue(false);
    		}
    	}
	}
	@Test
	public void getDevicesByDeviceOnly() {
		List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <Device> filteredDeviceList = deviceCalcTestCaseInstance.getDeviceListFiltered();
    	for(Device device : filteredDeviceList) {
    		assertTrue(filter.getDeviceIds().contains(device.getDeviceId()));
    	}
	}
	@Test
	public void getDevicesByFunctionOnly() {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchFunctions);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <Device> filteredDeviceList = deviceCalcTestCaseInstance.getDeviceListFiltered();
    	for(Device device : filteredDeviceList) {
    			boolean deviceFunctionsContainsSpecificFunction = false;
    			for (Function function : device.getFunctions()) {
    				if(filter.getFunctionIds().contains(function.getFunctionId())) {
    					deviceFunctionsContainsSpecificFunction = true;
    				}
    			}
    			assertTrue(deviceFunctionsContainsSpecificFunction);
    	}
	}
	@Test
	public void getDevicesByGroupOnly() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <Device> filteredDeviceList = deviceCalcTestCaseInstance.getDeviceListFiltered();
    	for(Device device : filteredDeviceList) {
    		assertTrue(!Collections.disjoint(filter.getGroupIds(), device.getGroupIds()));
    	}
	}
	@Test
	public void getDevicesByRoomOnly() {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <Device> filteredDeviceList = deviceCalcTestCaseInstance.getDeviceListFiltered();
    	for(Device device : filteredDeviceList) {
    		assertTrue(!Collections.disjoint(filter.getRoomIds(), device.getRoomIds()));
    	}
	}
	@Test
	public void getDevicesByMultipleDifferentFilters() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <Device> filteredDeviceList = deviceCalcTestCaseInstance.getDeviceListFiltered();
    	for(Device device : filteredDeviceList) {
    		assertTrue(!Collections.disjoint(filter.getRoomIds(), device.getRoomIds()) && !Collections.disjoint(filter.getGroupIds(), device.getGroupIds()));
    	}
	}
	@Test
	public void getAllDevicesByNoneFiltering() {
		DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(new Filter());
		List <Device> filteredDeviceList = deviceCalcTestCaseInstance.getDeviceListFiltered();
		
		assertTrue(filteredDeviceList.size() == io.swagger.api.calc.DeviceMockup.getDevicesMockup().size());
	}
	@Test
	public void getNoDevices() {
		List <String> searchDevices = Arrays.asList("non_existing_device");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <Device> filteredDeviceList = deviceCalcTestCaseInstance.getDeviceListFiltered();
    	assertTrue(filteredDeviceList.isEmpty());
	}
	@Test
	public void deviceIdsAsNone() {
		List <String> searchDevices = null;    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <Device> filteredDeviceList = deviceCalcTestCaseInstance.getDeviceListFiltered();
    	assertEquals(filteredDeviceList, io.swagger.api.calc.DeviceMockup.getDevicesMockup());		
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
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	filter.setFunctionIds(searchFunctions);
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	assertTrue(!Collections.disjoint(filteredDeviceList, searchFunctions));
	}
	@Test
	public void getFunctionsByDeviceOnly() {
		List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testfunction_0"));
    	assertTrue(filteredDeviceList.contains("testfunction_1"));
	}
	@Test
	public void getFunctionsByFunctionOnly() {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchFunctions);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchFunctions, filteredDeviceList));
	}
	@Test
	public void getFucntionsByGroupOnly() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testfunction_0"));
    	assertTrue(filteredDeviceList.contains("testfunction_1"));
	}
	@Test
	public void getFunctionsByRoomOnly() {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testfunction_0"));
    	assertTrue(filteredDeviceList.contains("testfunction_1"));
	}
	@Test
	public void getFunctionsByMultipleDifferentFilters() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testfunction_0"));
    	assertTrue(filteredDeviceList.contains("testfunction_1"));
	}
	@Test
	public void getAllFunctionsByNoneFiltering() {
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(new Filter());
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testfunction_0"));
    	assertTrue(filteredDeviceList.contains("testfunction_1"));
    	assertTrue(filteredDeviceList.contains("testfunction_2"));
	}
	@Test
	public void getNoFunctions() {
		List <String> searchDevices = Arrays.asList("non_existing_device");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	assertTrue(filteredDeviceList.isEmpty());
	}
	@Test
	public void functionIdsAsNone() {
		List <String> searchFunction = null;    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchFunction);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getFuntionsByDevicesFiltered();
    	assertTrue(!filteredDeviceList.isEmpty());		
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
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	filter.setFunctionIds(searchFunctions);
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	assertTrue(!Collections.disjoint(filteredDeviceList, searchGroups));
	}
	@Test
	public void getGroupsByDeviceOnly() {
		List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testgroup_0"));
    	assertTrue(filteredDeviceList.contains("testgroup_1"));
	}
	@Test
	public void getGroupsByFunctionOnly() {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchFunctions);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testgroup_0"));
	}
	@Test
	public void getGroupsByGroupOnly() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchGroups, filteredDeviceList));
	}
	@Test
	public void getGroupsByRoomOnly() {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testgroup_0"));
    	assertTrue(filteredDeviceList.contains("testgroup_1"));
	}
	@Test
	public void getGroupsByMultipleDifferentFilters() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchGroups, filteredDeviceList));
	}
	@Test
	public void getAllGroupsByNoneFiltering() {
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(new Filter());
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testgroup_0"));
    	assertTrue(filteredDeviceList.contains("testgroup_1"));
    	assertTrue(filteredDeviceList.contains("testgroup_2"));
	}
	@Test
	public void getNoGroups() {
		List <String> searchDevices = Arrays.asList("non_existing_device");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	assertTrue(filteredDeviceList.isEmpty());
	}
	@Test
	public void groupIdsAsNone() {
		List <String> searchGroup = null;    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchGroup);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	assertTrue(!filteredDeviceList.isEmpty());		
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
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	filter.setFunctionIds(searchFunctions);
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	assertTrue(!Collections.disjoint(filteredDeviceList, searchRooms));
	}
	@Test
	public void getRoomsByDeviceOnly() {
		List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testroom_0"));
    	assertTrue(filteredDeviceList.contains("testroom_1"));
	}
	@Test
	public void getRoomsByFunctionOnly() {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchFunctions);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testroom_0"));
	}
	@Test
	public void getRoomsByGroupOnly() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testroom_0"));
    	assertTrue(filteredDeviceList.contains("testroom_1"));
	}
	@Test
	public void getRoomsByRoomOnly() {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchRooms, filteredDeviceList));
	}
	@Test
	public void getRoomsByMultipleDifferentFilters() {
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(!Collections.disjoint(searchRooms, filteredDeviceList));
	}
	@Test
	public void getAllRoomsByNoneFiltering() {
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(new Filter());
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	
    	assertTrue(filteredDeviceList.contains("testroom_0"));
    	assertTrue(filteredDeviceList.contains("testroom_1"));
    	assertTrue(filteredDeviceList.contains("testroom_2"));
	}
	@Test
	public void getNoRooms() {
		List <String> searchDevices = Arrays.asList("non_existing_device");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	assertTrue(filteredDeviceList.isEmpty());
	}
	@Test
	public void roomIdsAsNone() {
		List <String> searchRoom = null;    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchRoom);
    	
    	DeviceCalcTestCase deviceCalcTestCaseInstance = new DeviceCalcTestCase(filter);
    	List <String> filteredDeviceList = deviceCalcTestCaseInstance.getGroupsByDevicesFiltered();
    	assertTrue(!filteredDeviceList.isEmpty());		
	}
}