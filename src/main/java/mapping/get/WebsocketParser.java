package mapping.get;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import io.swagger.model.Device;

public class WebsocketParser {
	private ObjectReader reader;
	private DeviceMapper mapper;
	private ModuleDescriptionLoader loader;
	
	public WebsocketParser() {
		
		this.reader = new ObjectMapper().reader();
		this.loader = new ModuleDescriptionLoader("module_descriptions");
		this.mapper = new DeviceMapper(loader);

	}
	
	public boolean update(String websocketMessage, Map<String, Device> deviceMap) {
		
		// TODO
		
		System.out.println("new websocket message: \n"+websocketMessage);
		
		return false;
	}
}
