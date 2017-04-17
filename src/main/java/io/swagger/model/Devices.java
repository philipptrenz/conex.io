package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Device;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
/**
 * Devices
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-12T11:08:27.892Z")

public class Devices   {
  @JsonProperty("devices")
  private List<Device> devices = new ArrayList<Device>();

  public Devices devices(List<Device> devices) {
    this.devices = devices;
    return this;
  }

  public Devices addDevicesItem(Device devicesItem) {
    this.devices.add(devicesItem);
    return this;
  }

   /**
   * Get devices
   * @return devices
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public List<Device> getDevices() {
    return devices;
  }

  public void setDevices(List<Device> devices) {
    this.devices = devices;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Devices devices = (Devices) o;
    return Objects.equals(this.devices, devices.devices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(devices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Devices {\n");
    
    sb.append("    devices: ").append(toIndentedString(devices)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

