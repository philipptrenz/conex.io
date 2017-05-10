package io.swagger;

import java.util.List;

import io.swagger.exception.HomeAutomationServerNotReachableException;
import io.swagger.model.Device;
import io.swagger.model.Function;

public interface HomeAutomationServerConnector {

	public List<Device> getDevices() throws HomeAutomationServerNotReachableException;
	
	public boolean setDevices(List<Device> devices, Function functionValuesToSet) throws HomeAutomationServerNotReachableException;
	
}
