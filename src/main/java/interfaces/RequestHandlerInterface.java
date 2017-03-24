package interfaces;

import io.swagger.model.Filter;
import io.swagger.model.Device;
import io.swagger.model.Function;

public interface RequestHandlerInterface {
	
	public Device[] getDevices(Filter filter);
	
	public String[] getRoomIds(Filter filter);
	
	public String[] getGroupIds(Filter filter);
	
	public String[] getFunctionIds(Filter filter);
	
	public boolean patchDevices(Filter filter, Function function);
}
