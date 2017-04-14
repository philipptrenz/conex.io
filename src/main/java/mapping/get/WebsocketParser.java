package mapping.get;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import io.swagger.model.Device;
import mapping.FHEMConnector;

public class WebsocketParser {
	private ObjectReader reader;
	private DeviceMapper mapper;
	private ModuleDescriptionLoader loader;
	
	public WebsocketParser() {
		
		this.reader = new ObjectMapper().reader();
		this.loader = new ModuleDescriptionLoader("module_descriptions");
		this.mapper = new DeviceMapper(loader);

	}
	
	public boolean update(String websocketMessage, Map<String, Device> deviceMap, FHEMConnector connector) {
		
		//test
		/*
		String test = "[\"testlampe\",\"on\",\"<div id=\\u0022testlampe\\u0022  title=\\u0022on\\u0022 class=\\u0022col2\\u0022><a href=\\u0022/fhem?cmd.testlampe=set testlampe off&room=testroom\\u0022><img class=' FS20_on' src=\\u0022/fhem/images/default/FS20.on.png\\u0022 alt=\\u0022on\\u0022 title=\\u0022on\\u0022></a></div>\"]\n[\"testlampe-state\",\"on\",\"on\"]\n[\"testlampe-state-ts\",\"2017-04-13 10:58:22\",\"2017-04-13 10:58:22\"]\n";
		
		System.out.println(test);
		if (parseWebsocketMessage(test) != null) {
			System.err.println("test works!");
		} else {
			System.err.println("test failed!");
		}
		
		// test end
		*/  
		
		DeviceUpdateMessage updateMessage = parseWebsocketMessage(websocketMessage);
		if (updateMessage != null) {
			
			System.out.println("yey! Device exists, time for mapping! Not yet implemented ...");
			
			if (updateMessage.deviceId.startsWith("#FHEMWEB")) {
				
				// just a FHEMWEB device update, ignore
				
				return false;
				
			} else if (updateMessage.deviceId.equals("global")) {
				
				// global status update from FHEM, could be important
				
				if (updateMessage.reading.equals("DEFINED")) {
					// new device defined, reload whole Map via jsonlist2!
					connector.reloadJsonlist2();
				}
				
				return false;
				
			} else {
				
				Device device = deviceMap.get(updateMessage.deviceId);
				
				if (device != null) {
					if (updateMessage.reading.toLowerCase().equals("room")) {
						
						// TODO: Update room
						
					} else if (updateMessage.reading.toLowerCase().equals("group")) {
						
						// TODO: Update group from map
						
					} else {
						
						// TODO: update values somehow ...
						
					}
				}
			}
		}
		
		
		return false;
	}
	
	private DeviceUpdateMessage parseWebsocketMessage(String message) {
				
		/*
		 * Example:
		 * 
		 * ["testlampe","on","<div id=\u0022testlampe\u0022  title=\u0022on\u0022 class=\u0022col2\u0022><a href=\u0022/fhem?cmd.testlampe=set testlampe off\u0022><img class=' FS20_on' src=\u0022/fhem/images/default/FS20.on.png\u0022 alt=\u0022on\u0022 title=\u0022on\u0022></a></div>"]
		 * ["testlampe-state","on","on"]
		 * ["testlampe-state-ts","2017-04-13 09:15:28","2017-04-13 09:15:28"]
		 * 
		 * Regex:
		 * 
		 * ^\["(.*)","(.*)","(.*)"\]\n\["(.*)","(.*)","(.*)"\]\n\["(.*)","(.*)","(.*)"\]\n*$
		 */
		
		String regex = "^\\[\"(.*)\",\"(.*)\",\"(.*)\"\\]\\n\\[\"(.*)\",\"(.*)\",\"(.*)\"\\]\\n\\[\"(.*)\",\"(.*)\",\"(.*)\"\\]\\n*$";
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(message);
		DeviceUpdateMessage deviceUpdateMessage = null;
		
		if (matcher.matches()) {
			
			
			/*
			 * Parse in format:
			 * 
			 * [ <deviceId>, - , - ]
			 * [ <reading>, <value> , - ]
			 * [ - , <timestamp> , - ]
			 */
			deviceUpdateMessage = new DeviceUpdateMessage();
			deviceUpdateMessage.deviceId = matcher.group(1);
			deviceUpdateMessage.reading = matcher.group(4).replace(deviceUpdateMessage.deviceId+"-", "");
			deviceUpdateMessage.value = matcher.group(5);
			deviceUpdateMessage.timestamp = matcher.group(8);
			
			System.out.println("matched: \n"+deviceUpdateMessage);
		}
		return deviceUpdateMessage;
	}
	
	
	private class DeviceUpdateMessage {
		public String deviceId;
		public String reading;
		public String value;
		public String timestamp;
		
		@Override
		public String toString() {
			String string = 
				"DeviceUpdateMessage {"+"\n"
				+ "\tdevice_id: "+deviceId+"\n"
				+ "\treading: "+reading+"\n"
				+ "\tvalue: "+value+"\n"
				+ "\ttimestamp: "+timestamp+"\n"
				+ "}";
			return string;
		}
		
	}
}
