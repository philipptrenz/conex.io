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

public class DeviceCalc {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Filter filter;
	private List<Device> geraete;

	/**
	 * Set's the Filter-Object and get the Devices from Mapping layer
	 * 
	 * @param filter
	 *            Filter-Object
	 */
	public DeviceCalc(Filter filter, List<Device> geraete) {
		if (filter != null) {
			this.filter = filter;
		} else {
			this.filter = new Filter();
		}
		this.geraete = geraete;
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
			for (int i = 0; i < geraete.size(); i++) {
				Device d = geraete.get(i);
				if (!isDeviceMatchingFiltering(d, filter)) {
					geraete.remove(i);
					i--;
				}
			}
		}
		log.info("Filtered " + geraete.size() + " devices");
		ausgabe = geraete;
		return ausgabe;
	}

	public List<Device> getDeviceListFilteringWithPatcherFunction(Function f) {
		if (f == null || f.getFunctionId() == null) {
			f = new Function();
			f.setFunctionId("null");
		}

		if (filter.getFunctionIds() == null) {
			filter.setFunctionIds(new ArrayList<>());
		}

		if (!filter.getFunctionIds().contains(f.getFunctionId())) {
			filter.addFunctionIdsItem(f.getFunctionId());
		}

		List<Device> list = getDeviceListFiltered();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				List<Function> functionList = list.get(i).getFunctions();
				boolean check = true;
				for (Function listenFunction : functionList) {
					if (listenFunction.getFunctionId().contains(f.getFunctionId())) {
						check = false;
					}
				}
				if (check) {
					list.remove(i);
					i--;
				}
			}
		}
		return list;
	}

	/**
	 * Filtering function for endpoint /functions
	 * 
	 * @return list of String Ids
	 */
	public List<String> getFuntionsByDevicesFiltered() {
		List<String> functions = new ArrayList<String>();
		for (Device d : getDeviceListFiltered()) {
			for (Function f : d.getFunctions()) {
				if (!functions.contains(f.getFunctionId())) {
					functions.add(f.getFunctionId());
				}
			}
		}
		return functions;
	}

	/**
	 * Filtering groups for endpoint /groups
	 * 
	 * @return list of String Ids
	 */
	public List<String> getGroupsByDevicesFiltered() {
		List<String> gruppen = new ArrayList<String>();
		for (Device d : getDeviceListFiltered()) {
			for (String group : d.getGroupIds()) {
				if (!gruppen.contains(group)) {
					gruppen.add(group);
				}
			}
		}
		return gruppen;
	}

	/**
	 * Filtering rooms for endpoint /rooms
	 * 
	 * @return list of String Ids
	 */
	public List<String> getRoomsByDevicesFiltered() {
		List<String> rooms = new ArrayList<String>();
		for (Device d : getDeviceListFiltered()) {
			for (String raum : d.getRoomIds()) {
				if (!rooms.contains(raum)) {
					rooms.add(raum);
				}
			}
		}
		return rooms;
	}

	/**
	 * Checks if a device matching all required filter attributes of the object
	 * filter
	 * 
	 * @param device
	 *            = The object to be filtered
	 * @param filter
	 *            = The filter object
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
			boolean matchingFunctions = false;
			for (Function func : device.getFunctions()) {
				if (filter.getFunctionIds().contains(func.getFunctionId())) {
					matchingFunctions = true;
				} else {
					// check also for inheritance
					for (String filterFunction : filter.getFunctionIds()) {						
						Class<?> c = MappingHelper.findFunctionClassByFunctionId(filterFunction);
						if (c == null) continue;
						
						if (c.isInstance(func)) {
							matchingFunctions = true;
						}
					}
				}
			}
			if (!matchingFunctions) {
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

	private boolean filterIsEmpty(Filter filter) {

		boolean devicesAreEmpty = (filter.getDeviceIds() == null || filter.getDeviceIds().isEmpty());
		boolean functionsAreEmpty = (filter.getFunctionIds() == null || filter.getFunctionIds().isEmpty());
		boolean roomsAreEmpty = (filter.getRoomIds() == null || filter.getRoomIds().isEmpty());
		boolean groupsAreEmpty = (filter.getGroupIds() == null || filter.getGroupIds().isEmpty());

		return devicesAreEmpty && functionsAreEmpty && roomsAreEmpty && groupsAreEmpty;
	}
}
