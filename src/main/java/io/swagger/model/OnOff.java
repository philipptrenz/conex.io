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
 * Generic On-Off Switch
 */
@ApiModel(description = "Generic On-Off Switch")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

public class OnOff extends Function  {
  @JsonProperty("isOn")
  private Boolean isOn = false;

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
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isOn, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OnOff {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    isOn: ").append(toIndentedString(isOn)).append("\n");
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

