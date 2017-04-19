package mapping.set;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Iterator;
import java.util.List;

import io.swagger.model.Device;
import io.swagger.model.Function;
import mapping.FHEMConnector;

public class FHEMCommandBuilder {

	private FHEMConnector connector;
	
	public FHEMCommandBuilder(FHEMConnector connector) {
		
		this.connector = connector;
		
	}
	
	public String buildCommand(List<Device> devices, Function functionValuesToSet) {
		
		/*
		 * TODO
		 * 
		 * check which values of Function to set (could be one or more)
		 * sort devices for each device type
		 * 
		 * for each device type:
		 * 		collect all deviceIds to String, separated by ","
		 *   	parse Function values to String
		 *   	for each value:
		 *   		build string "set "+allDeviceIds+" "+functionValue
		 * concatenate all commands via ";" (?)
		 */
		
		return null;
	}
	
}
