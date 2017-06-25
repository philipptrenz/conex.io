package io.swagger.api;

/**
 * The Class ApiException.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

public class ApiException extends Exception{
	
	/** The code. */
	private int code;
	
	/**
	 * Instantiates a new api exception.
	 *
	 * @param code the code
	 * @param msg the msg
	 */
	public ApiException (int code, String msg) {
		super(msg);
		this.code = code;
	}
}
