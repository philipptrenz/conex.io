package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Function;
import java.math.BigDecimal;
import org.joda.time.DateTime;
import javax.validation.constraints.*;
/**
 * Generic Temperature Sensor
 */
@ApiModel(description = "Generic Temperature Sensor")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-12T10:59:29.046Z")

public class Temperature extends Function  {
  @JsonProperty("value")
  private BigDecimal value = null;

  @JsonProperty("timestamp")
  private DateTime timestamp = null;

  public Temperature value(BigDecimal value) {
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
  @DecimalMin("0")
  @DecimalMax("512")
  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public Temperature timestamp(DateTime timestamp) {
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
    Temperature temperature = (Temperature) o;
    return Objects.equals(this.value, temperature.value) &&
        Objects.equals(this.timestamp, temperature.timestamp) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, timestamp, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Temperature {\n");
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

