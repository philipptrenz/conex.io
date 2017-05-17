package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Dimmer;
import org.joda.time.DateTime;
import javax.validation.constraints.*;
/**
 * Generic color dimmer using the hsv color model
 */
@ApiModel(description = "Generic color dimmer using the hsv color model")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-17T12:31:36.374+02:00")

public class ColorDimmer extends Dimmer  {
  @JsonProperty("hue")
  private Integer hue = null;

  @JsonProperty("saturation")
  private Integer saturation = null;

  public ColorDimmer hue(Integer hue) {
    this.hue = hue;
    return this;
  }

   /**
   * Color value in degrees
   * minimum: 0
   * maximum: 359
   * @return hue
  **/
  @ApiModelProperty(value = "Color value in degrees")
  @Min(0)
  @Max(359)
  public Integer getHue() {
    return hue;
  }

  public void setHue(Integer hue) {
    this.hue = hue;
  }

  public ColorDimmer saturation(Integer saturation) {
    this.saturation = saturation;
    return this;
  }

   /**
   * Saturation of color
   * minimum: 0
   * maximum: 255
   * @return saturation
  **/
  @ApiModelProperty(value = "Saturation of color")
  @Min(0)
  @Max(255)
  public Integer getSaturation() {
    return saturation;
  }

  public void setSaturation(Integer saturation) {
    this.saturation = saturation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColorDimmer colorDimmer = (ColorDimmer) o;
    return Objects.equals(this.hue, colorDimmer.hue) &&
        Objects.equals(this.saturation, colorDimmer.saturation) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hue, saturation, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ColorDimmer {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    hue: ").append(toIndentedString(hue)).append("\n");
    sb.append("    saturation: ").append(toIndentedString(saturation)).append("\n");
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

