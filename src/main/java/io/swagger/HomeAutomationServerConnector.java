/*
 * 
 */
package io.swagger;

import java.util.List;

import io.swagger.exception.HomeAutomationServerNotReachableException;
import io.swagger.model.Device;
import io.swagger.model.Function;

/**
 * The Interface HomeAutomationServerConnector.
 * 
 * This interface provides the methods to connect a home automation server 
 * connector class to the endpoints. Therefore the connector class, i.e.
 * FHEMConnector, has to implement this interface and get annotated as 
 * '@Service'.
 * 
 * @author Philipp Trenz
 */
public interface HomeAutomationServerConnector {

	/**
	 * Gets the devices.
	 *
	 * @return the devices
	 * @throws HomeAutomationServerNotReachableException the home automation server not reachable exception
	 */
	public List<Device> getDevices() throws HomeAutomationServerNotReachableException;
	
	/**
	 * Sets the devices.
	 *
	 * @param devices the devices
	 * @param functionValuesToSet the function values to set
	 * @return true, if successful
	 * @throws HomeAutomationServerNotReachableException the home automation server not reachable exception
	 */
	public boolean setDevices(List<Device> devices, Function functionValuesToSet) throws HomeAutomationServerNotReachableException;
	
}
