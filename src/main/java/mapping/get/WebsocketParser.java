package mapping.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.model.Device;
import mapping.FHEMConnector;
import mapping.get.functionMapper.FunctionMapper;

public class WebsocketParser {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ModuleDescriptionLoader loader;
	private FunctionMapper mapper;
	private DeviceMapper deviceMapper;
	
	public WebsocketParser() {
		this.loader = new ModuleDescriptionLoader("module_descriptions");
		this.mapper = new FunctionMapper();
		this.deviceMapper = new DeviceMapper(loader);
	}
	
	public void update(String websocketMessage, Map<String, Device> deviceMap, FHEMConnector connector) {
		
		List<WebsocketDeviceUpdateMessage> updateMessageList = parseWebsocketMessage(websocketMessage);
		
		for (WebsocketDeviceUpdateMessage updateMessage : updateMessageList) {
			if (updateMessage != null && updateMessage.deviceId != null) {
				
				if (updateMessage.deviceId.startsWith("#FHEMWEB")) {
					
					// just a FHEMWEB device update, ignore
					continue;
					
				} else if (updateMessage.deviceId.equals("global")) {
					
					handleFHEMGlobalEvent(updateMessage, connector);
					continue;
					
				} else if (deviceMap.containsKey(updateMessage.deviceId.toLowerCase())) {
					
					Device device = deviceMap.get(updateMessage.deviceId.toLowerCase());
					if (isParsable(updateMessage)) {
						String typeId = device.getTypeId();
						JsonNode moduleDescription = loader.getModuleDescription(typeId);
						
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
							log.info("Updated device with device_id '"+device.getDeviceId()+"' from FHEM via websocket");
						}
					}
					
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
	}

	private List<WebsocketDeviceUpdateMessage> parseWebsocketMessage(String message) {
		
		List<WebsocketDeviceUpdateMessage> deviceUpdateMessages = new ArrayList<>();
		
		/*
		 * Example:
		 * 
		 * ["testlampe","on","<div id=\u0022testlampe\u0022 ... /></div>"]
		 * ["testlampe-state","on","on"]
		 * ["testlampe-state-ts","2017-04-13 09:15:28","2017-04-13 09:15:28"]
		 * ...
		 * 
		 * Regex for each line:
		 * 
		 * ^\["(.*)","(.*)","(.*)"\]$
		 */
		
		final String deviceId;
		String[] semiJsonArrays = message.split("\\n");
		
		Pattern pattern = Pattern.compile("\\[\"(.*)\",\"(.*)\",\"(.*)\"\\]");
		
		Matcher matcher = pattern.matcher(semiJsonArrays[0]);
		if(matcher.find()) {
			deviceId = matcher.group(1);
		} else {
			return deviceUpdateMessages;
		}
		
		for (int i = 1; i < semiJsonArrays.length; i++) {
			
			WebsocketDeviceUpdateMessage updateMessage = new WebsocketDeviceUpdateMessage(deviceId);
			boolean isOkay = false;
			String m1 = semiJsonArrays[i];
			String reading = "";
			
			matcher = pattern.matcher(m1);
			if (matcher.find()) {
				reading = matcher.group(1).replace(deviceId+"-", "");
				updateMessage.reading = reading;
				updateMessage.value = matcher.group(2);
				// matcher.group(3)); // ignore
				isOkay = true;
			}
			
			if ((i+1) < semiJsonArrays.length) {
				String m2 = semiJsonArrays[i+1];
				matcher = pattern.matcher(m2);
				if (matcher.find()) {
					if (matcher.group(1).equals(deviceId+"-"+reading+"-ts")) {
						updateMessage.timestamp = matcher.group(2);
						i++;	// step over to the string after this
					}
				}
			}
			if (isOkay) deviceUpdateMessages.add(updateMessage);
		}
		return deviceUpdateMessages;
	}
	
	private boolean isParsable(WebsocketDeviceUpdateMessage message) {
		return (message != null && message.deviceId != null && !message.deviceId.isEmpty() && message.reading != null && !message.reading.isEmpty() && message.value != null && !message.value.isEmpty() && message.timestamp != null && !message.timestamp.isEmpty());
	}
	
	private void handleFHEMGlobalEvent(WebsocketDeviceUpdateMessage updateMessage, FHEMConnector connector) {
		switch(updateMessage.reading) {
		
		case "INITIALIZED":
			// after initialization is finished.
			break;
			
		case "REREADCFG":
			// after the configuration is reread.
			connector.reload();
			break;
			
		case "SAVE":
			// before the configuration is saved.
			connector.reload();
			break;
			
		case "SHUTDOWN":
			// before FHEM is shut down.
			break;
			
		case "DEFINED":
			// after a device is defined.
			connector.reload();
			break;
			
		case "DELETED":
			// after a device was deleted.
			connector.reload();
			break;
			
		case "RENAMED":
			// after a device was renamed.
			connector.reload();
			break;
			
		case "UNDEFINED":
			// upon reception of a message for an undefined device.
			break;
			
		case "MODIFIED":
			// after a device modification.
			connector.reload();
			break;
			
		case "UPDATE":
			// after an update is completed.
			break;
			
		default: 
			connector.reload();
			break;
		}
	}
}
