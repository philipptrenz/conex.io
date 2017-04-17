package mapping.get;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.model.Device;
import mapping.FHEMConnector;
import mapping.get.functionMapper.FunctionMapper;

public class WebsocketParser {
	
	private ModuleDescriptionLoader loader;
	private FunctionMapper mapper;
	
	public WebsocketParser() {
		this.loader = new ModuleDescriptionLoader("module_descriptions");
		this.mapper = new FunctionMapper();
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
		
		WebsocketDeviceUpdateMessage updateMessage = parseWebsocketMessage(websocketMessage);
		if (updateMessage != null) {
			
			if (updateMessage.deviceId.startsWith("#FHEMWEB")) {
				
				// just a FHEMWEB device update, ignore
				
				return false;
				
			} else if (updateMessage.deviceId.equals("global")) {
				
				// global status update from FHEM, could be important
				
				if (updateMessage.reading.equals("DEFINED")) {
					// new device defined, reload whole Map via jsonlist2!
					System.out.println("reload jsonlist2 data");
					connector.reload();
				}
				
				return false;
				
			} else {
				
				Device device = deviceMap.get(updateMessage.deviceId);
				
				if (device != null) {
					
					if (updateMessage.reading.toLowerCase().equals("room")) {
						
						// TODO: Update room
						System.out.println("TODO: Update room");
						
					} else if (updateMessage.reading.toLowerCase().equals("group")) {
						
						// TODO: Update group from map
						System.out.println("TODO: Update group");
						
					} else {
						
						// TODO: update values somehow ...
						//System.out.println("TODO: Update '"+updateMessage.reading+"' @ '"+updateMessage.deviceId+"': value: '"+updateMessage.value+"', timestamp: '"+updateMessage.timestamp+"'");
						
						JsonNode moduleDescription = loader.getModuleDescription(device.getTypeId());
						
						if (moduleDescription != null) {
							mapper.mapWebsocketValuesToFunction(device, updateMessage, moduleDescription);
						}
						
					}
				}
			}
		}
		
		
		return false;
	}
	
	private WebsocketDeviceUpdateMessage parseWebsocketMessage(String message) {
				
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
		WebsocketDeviceUpdateMessage deviceUpdateMessage = null;
		
		if (matcher.matches()) {
			
			
			/*
			 * Parse in format:
			 * 
			 * [ <deviceId>, - , - ]
			 * [ <reading>, <value> , - ]
			 * [ - , <timestamp> , - ]
			 */
			deviceUpdateMessage = new WebsocketDeviceUpdateMessage();
			deviceUpdateMessage.deviceId = matcher.group(1);
			deviceUpdateMessage.reading = matcher.group(4).replace(deviceUpdateMessage.deviceId+"-", "");
			deviceUpdateMessage.value = matcher.group(5);
			deviceUpdateMessage.timestamp = matcher.group(8);
		}
		return deviceUpdateMessage;
	}
}
