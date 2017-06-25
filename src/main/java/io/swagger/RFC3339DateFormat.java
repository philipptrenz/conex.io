/*
 * 
 */
package io.swagger;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.ISO8601Utils;

import java.text.FieldPosition;
import java.util.Date;

/**
 * The Class RFC3339DateFormat.
 * 
 * Helper class to map Date format.
 */
public class RFC3339DateFormat extends ISO8601DateFormat {

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.util.ISO8601DateFormat#format(java.util.Date, java.lang.StringBuffer, java.text.FieldPosition)
   */
  // Same as ISO8601DateFormat but serializing milliseconds.
  @Override
  public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
    String value = ISO8601Utils.format(date, true);
    toAppendTo.append(value);
    return toAppendTo;
  }

}