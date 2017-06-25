package io.swagger.api;

/**
 * The Class NotFoundException.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

public class NotFoundException extends ApiException {
	
	/** The code. */
	private int code;
	
	/**
	 * Instantiates a new not found exception.
	 *
	 * @param code the code
	 * @param msg the msg
	 */
	public NotFoundException (int code, String msg) {
		super(code, msg);
		this.code = code;
	}
}
