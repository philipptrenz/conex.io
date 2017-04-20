package mapping.get;

public class WebsocketDeviceUpdateMessage {
	
	public String deviceId;
	public String reading;
	public String value;
	public String timestamp;
	
	public WebsocketDeviceUpdateMessage(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((reading == null) ? 0 : reading.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof WebsocketDeviceUpdateMessage))
			return false;
		WebsocketDeviceUpdateMessage other = (WebsocketDeviceUpdateMessage) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (reading == null) {
			if (other.reading != null)
				return false;
		} else if (!reading.equals(other.reading))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
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