package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
/**
 * Filter
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

public class Filter   {
  @JsonProperty("device_ids")
  private List<String> deviceIds = new ArrayList<String>();

  @JsonProperty("room_ids")
  private List<String> roomIds = new ArrayList<String>();

  @JsonProperty("group_ids")
  private List<String> groupIds = new ArrayList<String>();

  @JsonProperty("function_ids")
  private List<String> functionIds = new ArrayList<String>();

  public Filter deviceIds(List<String> deviceIds) {
    this.deviceIds = deviceIds;
    return this;
  }

  public Filter addDeviceIdsItem(String deviceIdsItem) {
    this.deviceIds.add(deviceIdsItem);
    return this;
  }

   /**
   * Get deviceIds
   * @return deviceIds
  **/
  @ApiModelProperty(value = "")
  public List<String> getDeviceIds() {
    return deviceIds;
  }

  public void setDeviceIds(List<String> deviceIds) {
    this.deviceIds = deviceIds;
  }

  public Filter roomIds(List<String> roomIds) {
    this.roomIds = roomIds;
    return this;
  }

  public Filter addRoomIdsItem(String roomIdsItem) {
    this.roomIds.add(roomIdsItem);
    return this;
  }

   /**
   * Get roomIds
   * @return roomIds
  **/
  @ApiModelProperty(value = "")
  public List<String> getRoomIds() {
    return roomIds;
  }

  public void setRoomIds(List<String> roomIds) {
    this.roomIds = roomIds;
  }

  public Filter groupIds(List<String> groupIds) {
    this.groupIds = groupIds;
    return this;
  }

  public Filter addGroupIdsItem(String groupIdsItem) {
    this.groupIds.add(groupIdsItem);
    return this;
  }

   /**
   * Get groupIds
   * @return groupIds
  **/
  @ApiModelProperty(value = "")
  public List<String> getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(List<String> groupIds) {
    this.groupIds = groupIds;
  }

  public Filter functionIds(List<String> functionIds) {
    this.functionIds = functionIds;
    return this;
  }

  public Filter addFunctionIdsItem(String functionIdsItem) {
    this.functionIds.add(functionIdsItem);
    return this;
  }

   /**
   * Get functionIds
   * @return functionIds
  **/
  @ApiModelProperty(value = "")
  public List<String> getFunctionIds() {
    return functionIds;
  }

  public void setFunctionIds(List<String> functionIds) {
    this.functionIds = functionIds;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Filter filter = (Filter) o;
    return Objects.equals(this.deviceIds, filter.deviceIds) &&
        Objects.equals(this.roomIds, filter.roomIds) &&
        Objects.equals(this.groupIds, filter.groupIds) &&
        Objects.equals(this.functionIds, filter.functionIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceIds, roomIds, groupIds, functionIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Filter {\n");
    
    sb.append("    deviceIds: ").append(toIndentedString(deviceIds)).append("\n");
    sb.append("    roomIds: ").append(toIndentedString(roomIds)).append("\n");
    sb.append("    groupIds: ").append(toIndentedString(groupIds)).append("\n");
    sb.append("    functionIds: ").append(toIndentedString(functionIds)).append("\n");
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

