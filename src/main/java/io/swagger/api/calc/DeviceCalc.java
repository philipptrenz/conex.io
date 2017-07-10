package io.swagger.api.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.model.Device;
import io.swagger.model.Filter;
import io.swagger.model.Function;
import mapping.MappingHelper;

/**
 * The Class DeviceCalc.
 */
public class DeviceCalc {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/** The filter. */
	private Filter filter;
	
	/** The filtered devices. */
	private List<Device> filteredDevices;

	/**
	 * Set's the Filter-Object and get the Devices from Mapping layer.
	 *
	 * @param filter            Filter-Object
	 * @param geraete the geraete
	 */
	public DeviceCalc(Filter filter, List<Device> geraete) {
		if (filter != null) {
			this.filter = filter;
		} else {
			this.filter = new Filter();
		}
		this.filteredDevices = geraete;
	}

	/**
	 * Filtering for all Devices and endpoint /devices. Iterates through all
	 * Filter-functions - if required.
	 * 
	 * @return list of Device's
	 */
	public List<Device> getDeviceListFiltered() {
		List<Device> ausgabe;
		if (!filterIsEmpty(filter)) {
			for (int deviceFilterCounter = 0; deviceFilterCounter < filteredDevices.size(); deviceFilterCounter++) {
				Device d = filteredDevices.get(deviceFilterCounter);
				if (!isDeviceMatchingFiltering(d, filter)) {
					filteredDevices.remove(deviceFilterCounter);
					deviceFilterCounter--;
				}
			}
		}
		log.info("Filtered " + filteredDevices.size() + " devices");
		ausgabe = filteredDevices;
		return ausgabe;
	}

	/**
	 * Gets the device list filtering with patcher function.
	 *
	 * @param function the function
	 * @return the device list filtering with patcher function
	 */
	public List<Device> getDeviceListFilteringWithPatcherFunction(Function function) {
		/*if (function == null || function != null || function.getFunctionId() == null) {
			function = new Function();
			function.setFunctionId("null");
		}*/

		if (filter.getFunctionIds() == null) {
			filter.setFunctionIds(new ArrayList<>());
		}

		if (!filter.getFunctionIds().contains(function.getFunctionId())) {
			filter.addFunctionIdsItem(function.getFunctionId());
		}

		List<Device> deviveFilteredList = getDeviceListFiltered();
		if (!deviveFilteredList.isEmpty()) {
			for (int deviceListPatchCounter = 0; deviceListPatchCounter < deviveFilteredList.size(); deviceListPatchCounter++) {
				List<Function> functionList = deviveFilteredList.get(deviceListPatchCounter).getFunctions();
				boolean deviceContainsFunction = true;
				for (Function listenFunction : functionList) {
					if (listenFunction.getFunctionId().contains(function.getFunctionId())) {
						deviceContainsFunction = false;
					}
				}
				if (deviceContainsFunction) {
					deviveFilteredList.remove(deviceListPatchCounter);
					deviceListPatchCounter--;
				}
			}
		}
		return deviveFilteredList;
	}

	/**
	 * Filtering function for endpoint /functions.
	 *
	 * @return list of String Ids
	 */
	public List<String> getFuntionsByDevicesFiltered() {
		List<String> filteredFunctions = new ArrayList<String>();
		for (Device device : getDeviceListFiltered()) {
			for (Function function : device.getFunctions()) {
				if (!filteredFunctions.contains(function.getFunctionId())) {
					filteredFunctions.add(function.getFunctionId());
				}
			}
		}
		return filteredFunctions;
	}

	/**
	 * Filtering groups for endpoint /groups.
	 *
	 * @return list of String Ids
	 */
	public List<String> getGroupsByDevicesFiltered() {
		List<String> filteredGroups = new ArrayList<String>();
		for (Device device : getDeviceListFiltered()) {
			for (String group : device.getGroupIds()) {
				if (!filteredGroups.contains(group)) {
					filteredGroups.add(group);
				}
			}
		}
		return filteredGroups;
	}

	/**
	 * Filtering rooms for endpoint /rooms.
	 *
	 * @return list of String Ids
	 */
	public List<String> getRoomsByDevicesFiltered() {
		List<String> filteredRooms = new ArrayList<String>();
		for (Device device : getDeviceListFiltered()) {
			for (String room : device.getRoomIds()) {
				if (!filteredRooms.contains(room)) {
					filteredRooms.add(room);
				}
			}
		}
		return filteredRooms;
	}

	/**
	 * Checks if a device matching all required filter attributes of the object
	 * filter.
	 *
	 * @param device            = The object to be filtered
	 * @param filter            = The filter object
	 * @return true if the device matches all required filter attributes.
	 *         Otherwise return false
	 */
	public static boolean isDeviceMatchingFiltering(Device device, Filter filter) {
		if (filter.getDeviceIds() != null && !filter.getDeviceIds().isEmpty()) {
			if (!filter.getDeviceIds().contains(device.getDeviceId())) {
				return false;
			}
		}
		if (filter.getFunctionIds() != null && !filter.getFunctionIds().isEmpty()) {
			boolean deviceFunctionsContaintsFunction = false;
			for (Function function : device.getFunctions()) {
				if (filter.getFunctionIds().contains(function.getFunctionId())) {
					deviceFunctionsContaintsFunction = true;
				} else {
					// check also for inheritance
					for (String filterFunction : filter.getFunctionIds()) {						
						Class<?> mappedClassbyFunctionId = MappingHelper.findFunctionClassByFunctionId(filterFunction);
						if (mappedClassbyFunctionId == null) continue;
						
						if (mappedClassbyFunctionId.isInstance(function)) {
							deviceFunctionsContaintsFunction = true;
						}
					}
				}
			}
			if (!deviceFunctionsContaintsFunction) {
				return false;
			}
		}
		if (filter.getRoomIds() != null && !filter.getRoomIds().isEmpty()) {
			if (Collections.disjoint(filter.getRoomIds(), device.getRoomIds())) {
				return false;
			}
		}
		if (filter.getGroupIds() != null && !filter.getGroupIds().isEmpty()) {
			if (Collections.disjoint(filter.getGroupIds(), device.getGroupIds())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Filter is empty.
	 *
	 * @param filter the filter
	 * @return true, if successful
	 */
	private boolean filterIsEmpty(Filter filter) {

		boolean devicesAreEmpty = (filter.getDeviceIds() == null || filter.getDeviceIds().isEmpty());
		boolean functionsAreEmpty = (filter.getFunctionIds() == null || filter.getFunctionIds().isEmpty());
		boolean roomsAreEmpty = (filter.getRoomIds() == null || filter.getRoomIds().isEmpty());
		boolean groupsAreEmpty = (filter.getGroupIds() == null || filter.getGroupIds().isEmpty());

		return devicesAreEmpty && functionsAreEmpty && roomsAreEmpty && groupsAreEmpty;
	}
}
