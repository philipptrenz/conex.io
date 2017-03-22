package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Function;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
/**
 * Device
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

public class Device   {
  @JsonProperty("device_id")
  private String deviceId = null;

  @JsonProperty("room_ids")
  private List<String> roomIds = new ArrayList<String>();

  @JsonProperty("group_ids")
  private List<String> groupIds = new ArrayList<String>();

  @JsonProperty("functions")
  private List<Function> functions = new ArrayList<Function>();

  public Device deviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

   /**
   * Get deviceId
   * @return deviceId
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public Device roomIds(List<String> roomIds) {
    this.roomIds = roomIds;
    return this;
  }

  public Device addRoomIdsItem(String roomIdsItem) {
    this.roomIds.add(roomIdsItem);
    return this;
  }

   /**
   * Get roomIds
   * @return roomIds
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public List<String> getRoomIds() {
    return roomIds;
  }

  public void setRoomIds(List<String> roomIds) {
    this.roomIds = roomIds;
  }

  public Device groupIds(List<String> groupIds) {
    this.groupIds = groupIds;
    return this;
  }

  public Device addGroupIdsItem(String groupIdsItem) {
    this.groupIds.add(groupIdsItem);
    return this;
  }

   /**
   * Get groupIds
   * @return groupIds
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public List<String> getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(List<String> groupIds) {
    this.groupIds = groupIds;
  }

  public Device functions(List<Function> functions) {
    this.functions = functions;
    return this;
  }

  public Device addFunctionsItem(Function functionsItem) {
    this.functions.add(functionsItem);
    return this;
  }

   /**
   * Get functions
   * @return functions
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public List<Function> getFunctions() {
    return functions;
  }

  public void setFunctions(List<Function> functions) {
    this.functions = functions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Device device = (Device) o;
    return Objects.equals(this.deviceId, device.deviceId) &&
        Objects.equals(this.roomIds, device.roomIds) &&
        Objects.equals(this.groupIds, device.groupIds) &&
        Objects.equals(this.functions, device.functions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, roomIds, groupIds, functions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Device {\n");
    
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    roomIds: ").append(toIndentedString(roomIds)).append("\n");
    sb.append("    groupIds: ").append(toIndentedString(groupIds)).append("\n");
    sb.append("    functions: ").append(toIndentedString(functions)).append("\n");
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

