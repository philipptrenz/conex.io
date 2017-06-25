package mapping.get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.exception.HomeAutomationServerNotReachableException;
import io.swagger.model.Device;
import mapping.FHEMConnector;
import mapping.get.functionMapper.FunctionMapper;

/**
 * The Class WebsocketParser.
 * 
 * This class parses FHEM updates received via websockets and 
 * updates internal values of devices.
 * 
 * @author Philipp Trenz
 */
public class WebsocketParser {
	
	/** The log. */
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/** The loader. */
	private ModuleDescriptionLoader loader;
	
	/** The mapper. */
	private FunctionMapper mapper;
	
	/** The device mapper. */
	private DeviceMapper deviceMapper;
	
	/** The log info. */
	private String log_info = "";
	
	/**
	 * Instantiates a new websocket parser.
	 */
	public WebsocketParser() {
		this.loader = new ModuleDescriptionLoader();
		this.mapper = new FunctionMapper();
		this.deviceMapper = new DeviceMapper(loader);
	}
	
	/**
	 * Update.
	 *
	 * @param websocketMessage the websocket message
	 * @param deviceMap the device map
	 * @param connector the connector
	 */
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
					
				} else if (deviceMap.containsKey(updateMessage.deviceId)) {
					
					Device device = deviceMap.get(updateMessage.deviceId);
					log_info = "module: "+device.getTypeId();
					
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
									mapper.mapWebsocketValuesToFunction(device, updateMessage, moduleDescription, log_info);
								}
								
							}
							log.info("Updated device with device_id '"+device.getDeviceId()+"' from FHEM via websocket ("+log_info+")");
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

	/**
	 * Parses the websocket message.
	 *
	 * @param message the message
	 * @return the list
	 */
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
	
	/**
	 * Checks if message is parsable.
	 *
	 * @param message the message
	 * @return true, if is parsable
	 */
	private boolean isParsable(WebsocketDeviceUpdateMessage message) {
		return (message != null && message.deviceId != null && !message.deviceId.isEmpty() && message.reading != null && !message.reading.isEmpty() && message.value != null && !message.value.isEmpty() && message.timestamp != null && !message.timestamp.isEmpty());
	}
	
	/**
	 * Handle FHEM global event.
	 * 
	 * FHEM fires global events which get handled here. I.e. at a 'SAVE' 
	 * event we assume devices have been added or deleted so we trigger 
	 * a reload of all data from FHEM.
	 *
	 * @param updateMessage the update message
	 * @param connector the connector
	 */
	private void handleFHEMGlobalEvent(WebsocketDeviceUpdateMessage updateMessage, FHEMConnector connector) {
		switch(updateMessage.reading) {
		
		case "INITIALIZED":
			// after initialization is finished.
			break;
			
		case "REREADCFG":
			// after the configuration is reread.
			reloadFhemData(connector);
			break;
			
		case "SAVE":
			// before the configuration is saved.
			reloadFhemData(connector);
			break;
			
		case "SHUTDOWN":
			// before FHEM is shut down.
			break;
			
		case "DEFINED":
			// after a device is defined.
			reloadFhemData(connector);
			break;
			
		case "DELETED":
			// after a device was deleted.
			reloadFhemData(connector);
			break;
			
		case "RENAMED":
			// after a device was renamed.
			reloadFhemData(connector);
			break;
			
		case "UNDEFINED":
			// upon reception of a message for an undefined device.
			break;
			
		case "MODIFIED":
			// after a device modification.
			reloadFhemData(connector);
			break;
			
		case "UPDATE":
			// after an update is completed.
			break;
			
		default: 
			reloadFhemData(connector);
			break;
		}
	}
	
	/**
	 * Reload fhem data.
	 *
	 * @param connector the connector
	 */
	private void reloadFhemData(FHEMConnector connector) {
		try {
			connector.reload();
		} catch (HomeAutomationServerNotReachableException e) {
			log.error("While trying to reload data from FHEM an error occured", e);
		}
	}
}
