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
 * Generic color temperature
 */
@ApiModel(description = "Generic color temperature")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-17T12:31:36.374+02:00")

public class ColorTemperature extends Function  {
  @JsonProperty("ct")
  private BigDecimal ct = null;

  @JsonProperty("timestamp")
  private DateTime timestamp = null;

  public ColorTemperature ct(BigDecimal ct) {
    this.ct = ct;
    return this;
  }

   /**
   * Temperature value in Kelvin
   * minimum: 0
   * @return ct
  **/
  @ApiModelProperty(value = "Temperature value in Kelvin")
  @DecimalMin("0")
  public BigDecimal getCt() {
    return ct;
  }

  public void setCt(BigDecimal ct) {
    this.ct = ct;
  }

  public ColorTemperature timestamp(DateTime timestamp) {
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
    ColorTemperature colorTemperature = (ColorTemperature) o;
    return Objects.equals(this.ct, colorTemperature.ct) &&
        Objects.equals(this.timestamp, colorTemperature.timestamp) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ct, timestamp, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ColorTemperature {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    ct: ").append(toIndentedString(ct)).append("\n");
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

