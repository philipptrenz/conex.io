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
 * Chooser for RGB colored devices
 */
@ApiModel(description = "Chooser for RGB colored devices")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

public class RGBColorChooser extends Function  {
  @JsonProperty("brightness")
  private Integer brightness = null;

  @JsonProperty("r-channel")
  private Integer rChannel = null;

  @JsonProperty("g-channel")
  private Integer gChannel = null;

  @JsonProperty("b-channel")
  private Integer bChannel = null;

  public RGBColorChooser brightness(Integer brightness) {
    this.brightness = brightness;
    return this;
  }

   /**
   * Total Brightness value (master value)
   * minimum: 0
   * maximum: 256
   * @return brightness
  **/
  @ApiModelProperty(value = "Total Brightness value (master value)")
  @Min(0)
  @Max(256)
  public Integer getBrightness() {
    return brightness;
  }

  public void setBrightness(Integer brightness) {
    this.brightness = brightness;
  }

  public RGBColorChooser rChannel(Integer rChannel) {
    this.rChannel = rChannel;
    return this;
  }

   /**
   * Intesity of red channel
   * minimum: 0
   * maximum: 256
   * @return rChannel
  **/
  @ApiModelProperty(value = "Intesity of red channel")
  @Min(0)
  @Max(256)
  public Integer getRChannel() {
    return rChannel;
  }

  public void setRChannel(Integer rChannel) {
    this.rChannel = rChannel;
  }

  public RGBColorChooser gChannel(Integer gChannel) {
    this.gChannel = gChannel;
    return this;
  }

   /**
   * Intesity of green channel
   * minimum: 0
   * maximum: 256
   * @return gChannel
  **/
  @ApiModelProperty(value = "Intesity of green channel")
  @Min(0)
  @Max(256)
  public Integer getGChannel() {
    return gChannel;
  }

  public void setGChannel(Integer gChannel) {
    this.gChannel = gChannel;
  }

  public RGBColorChooser bChannel(Integer bChannel) {
    this.bChannel = bChannel;
    return this;
  }

   /**
   * Intesity of blue channel
   * minimum: 0
   * maximum: 256
   * @return bChannel
  **/
  @ApiModelProperty(value = "Intesity of blue channel")
  @Min(0)
  @Max(256)
  public Integer getBChannel() {
    return bChannel;
  }

  public void setBChannel(Integer bChannel) {
    this.bChannel = bChannel;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RGBColorChooser rgBColorChooser = (RGBColorChooser) o;
    return Objects.equals(this.brightness, rgBColorChooser.brightness) &&
        Objects.equals(this.rChannel, rgBColorChooser.rChannel) &&
        Objects.equals(this.gChannel, rgBColorChooser.gChannel) &&
        Objects.equals(this.bChannel, rgBColorChooser.bChannel) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(brightness, rChannel, gChannel, bChannel, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RGBColorChooser {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    brightness: ").append(toIndentedString(brightness)).append("\n");
    sb.append("    rChannel: ").append(toIndentedString(rChannel)).append("\n");
    sb.append("    gChannel: ").append(toIndentedString(gChannel)).append("\n");
    sb.append("    bChannel: ").append(toIndentedString(bChannel)).append("\n");
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

