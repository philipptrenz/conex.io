package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Function;
import org.joda.time.DateTime;
import javax.validation.constraints.*;
/**
 * Generic Slider Switch
 */
@ApiModel(description = "Generic Slider Switch")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-12T10:59:29.046Z")

public class Dimmer extends Function  {
  @JsonProperty("value")
  private Integer value = null;

  @JsonProperty("timestamp")
  private DateTime timestamp = null;

  public Dimmer value(Integer value) {
    this.value = value;
    return this;
  }

   /**
   * Get value
   * minimum: 0
   * maximum: 512
   * @return value
  **/
  @ApiModelProperty(value = "")
  @Min(0)
  @Max(512)
  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public Dimmer timestamp(DateTime timestamp) {
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
    Dimmer dimmer = (Dimmer) o;
    return Objects.equals(this.value, dimmer.value) &&
        Objects.equals(this.timestamp, dimmer.timestamp) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, timestamp, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dimmer {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

