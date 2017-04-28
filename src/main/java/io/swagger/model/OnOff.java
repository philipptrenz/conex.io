package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Function;
import org.joda.time.DateTime;

/**
 * Generic On-Off switch
 */
@ApiModel(description = "Generic On-Off switch")
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2017-04-28T08:38:34.207Z")

public class OnOff extends Function  {
  @JsonProperty("isOn")
  private Boolean isOn = false;

  @JsonProperty("timestamp")
  private DateTime timestamp = null;

  public OnOff isOn(Boolean isOn) {
    this.isOn = isOn;
    return this;
  }

   /**
   * Get isOn
   * @return isOn
  **/
  @ApiModelProperty(value = "")
  public Boolean getIsOn() {
    return isOn;
  }

  public void setIsOn(Boolean isOn) {
    this.isOn = isOn;
  }

  public OnOff timestamp(DateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

   /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(value = "")
  public DateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(DateTime timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OnOff onOff = (OnOff) o;
    return Objects.equals(this.isOn, onOff.isOn) &&
        Objects.equals(this.timestamp, onOff.timestamp) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isOn, timestamp, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OnOff {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    isOn: ").append(toIndentedString(isOn)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

