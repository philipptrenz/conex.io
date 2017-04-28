package mapping;

import java.util.List;

import io.swagger.model.Device;
import io.swagger.model.Function;

public interface AutomationServerConnector {

	public List<Device> getDevices();
	
	public boolean setDevices(List<Device> devices, Function functionValuesToSet);
	
}
