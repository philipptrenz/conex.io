package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.model.Filter;
import io.swagger.model.Function;
import javax.validation.constraints.*;
/**
 * Patcher
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-12T10:59:29.046Z")

public class Patcher   {
  @JsonProperty("filter")
  private Filter filter = null;

  @JsonProperty("function")
  private Function function = null;

  public Patcher filter(Filter filter) {
    this.filter = filter;
    return this;
  }

   /**
   * Get filter
   * @return filter
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public Patcher function(Function function) {
    this.function = function;
    return this;
  }

   /**
   * Get function
   * @return function
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public Function getFunction() {
    return function;
  }

  public void setFunction(Function function) {
    this.function = function;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Patcher patcher = (Patcher) o;
    return Objects.equals(this.filter, patcher.filter) &&
        Objects.equals(this.function, patcher.function);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filter, function);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Patcher {\n");
    
    sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
    sb.append("    function: ").append(toIndentedString(function)).append("\n");
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

