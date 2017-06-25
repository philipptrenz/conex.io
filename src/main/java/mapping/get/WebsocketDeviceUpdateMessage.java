package mapping.get;

/**
 * The Class WebsocketDeviceUpdateMessage.
 * 
 * This model class gets used to store the ugly websocket update messages
 * in a readable format. 
 * 
 * @author Philipp Trenz
 */
public class WebsocketDeviceUpdateMessage {
	
	/** The device id. */
	public String deviceId;
	
	/** The reading. */
	public String reading;
	
	/** The value. */
	public String value;
	
	/** The timestamp. */
	public String timestamp;
	
	/**
	 * Instantiates a new websocket device update message.
	 *
	 * @param deviceId the device id
	 */
	public WebsocketDeviceUpdateMessage(String deviceId) {
		this.deviceId = deviceId;
	}
	
	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the reading.
	 *
	 * @return the reading
	 */
	public String getReading() {
		return reading;
	}

	/**
	 * Sets the reading.
	 *
	 * @param reading the new reading
	 */
	public void setReading(String reading) {
		this.reading = reading;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp the new timestamp
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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