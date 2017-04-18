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
	private DeviceMapper deviceMapper;
	
	public WebsocketParser() {
		this.loader = new ModuleDescriptionLoader("module_descriptions");
		this.mapper = new FunctionMapper();
		this.deviceMapper = new DeviceMapper(loader);
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
		if (updateMessage != null && updateMessage.deviceId != null) {
			
			if (updateMessage.deviceId.startsWith("#FHEMWEB")) {
				
				// just a FHEMWEB device update, ignore
				return false;
				
			} else if (updateMessage.deviceId.equals("global")) {
				
				handleFHEMGlobalEvent(updateMessage, connector);
				return false;
				
			} else {
				
				Device device = deviceMap.get(updateMessage.deviceId);
				if (isParsable(updateMessage)) {
					JsonNode moduleDescription = loader.getModuleDescription(device.getTypeId());
					
					if (moduleDescription != null) {
						
						if (updateMessage.reading.toLowerCase().equals("room")) {
							
							device.setRoomIds(deviceMapper.mapRoomIds(updateMessage.value, moduleDescription));
							
						} else if (updateMessage.reading.toLowerCase().equals("group")) {
							
							device.setGroupIds(deviceMapper.mapGroupIds(updateMessage.value, moduleDescription));
							
						} else {

							if (moduleDescription != null) {
								mapper.mapWebsocketValuesToFunction(device, updateMessage, moduleDescription);
							}
							
						}
						
						System.out.println("updated device:\n"+device);
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
		
		String regex = "^\\[\"(.*)\",\"(.*)\",\"(.*)\"\\](\\n\\[\"(.*)\",\"(.*)\",\"(.*)\"\\](\\n\\[\"(.*)\",\"(.*)\",\"(.*)\"\\])?)?\\n*$";
		
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
			
			try {
				deviceUpdateMessage.deviceId = matcher.group(1) != null ? matcher.group(1) : null;
				
				deviceUpdateMessage.reading = matcher.group(5) != null ? matcher.group(5) : null;
				if (deviceUpdateMessage.reading != null && deviceUpdateMessage.deviceId != null) deviceUpdateMessage.reading = deviceUpdateMessage.reading.replace(deviceUpdateMessage.deviceId+"-", "");
				
				deviceUpdateMessage.value = matcher.group(6) != null ? matcher.group(6) : null;
				
				deviceUpdateMessage.timestamp = matcher.group(10) != null ? matcher.group(10) : null;
			}catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return deviceUpdateMessage;
	}
	
	private boolean isParsable(WebsocketDeviceUpdateMessage message) {
		return (message != null && message.deviceId != null && !message.deviceId.isEmpty() && message.reading != null && !message.reading.isEmpty() && message.value != null && !message.value.isEmpty() && message.timestamp != null && !message.timestamp.isEmpty());
	}
	
	private void handleFHEMGlobalEvent(WebsocketDeviceUpdateMessage updateMessage, FHEMConnector connector) {
		switch(updateMessage.reading) {
		
		case "DEFINED":
			System.out.println("new device defined, reload jsonlist2 data");
			connector.reload();
			break;
			
		case "DELETEATTR":
			System.out.println("attribute deleted, reload jsonlist2 data");
			connector.reload();
			break;
			
		default: 
			System.out.println("'"+updateMessage.reading+" happend, preventive reload");
			connector.reload();
			break;
		}
	}
}
