package mapping.get;

public class WebsocketDeviceUpdateMessage {
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