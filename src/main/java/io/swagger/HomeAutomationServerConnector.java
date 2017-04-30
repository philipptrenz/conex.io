package io.swagger;

import java.util.List;

import io.swagger.model.Device;
import io.swagger.model.Function;

public interface HomeAutomationServerConnector {

	public List<Device> getDevices();
	
	public boolean setDevices(List<Device> devices, Function functionValuesToSet);
	
}
