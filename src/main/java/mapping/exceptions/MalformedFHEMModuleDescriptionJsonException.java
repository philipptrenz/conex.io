/*
 * 
 */
package mapping.exceptions;

/**
 * The Class MalformedFHEMModuleDescriptionJsonException.
 */
public class MalformedFHEMModuleDescriptionJsonException extends Exception {
	 
 	/**
 	 * Instantiates a new malformed FHEM module description json exception.
 	 */
 	public MalformedFHEMModuleDescriptionJsonException() { super(); }
	 
 	/**
 	 * Instantiates a new malformed FHEM module description json exception.
 	 *
 	 * @param message the message
 	 */
 	public MalformedFHEMModuleDescriptionJsonException(String message) { super(message); }
	 
 	/**
 	 * Instantiates a new malformed FHEM module description json exception.
 	 *
 	 * @param message the message
 	 * @param cause the cause
 	 */
 	public MalformedFHEMModuleDescriptionJsonException(String message, Throwable cause) { super(message, cause); }
	 
 	/**
 	 * Instantiates a new malformed FHEM module description json exception.
 	 *
 	 * @param cause the cause
 	 */
 	public MalformedFHEMModuleDescriptionJsonException(Throwable cause) { super(cause); }
	 
	 
	 /**
 	 * Instantiates a new malformed FHEM module description json exception.
 	 *
 	 * @param function the function
 	 * @param folder the folder
 	 */
 	public MalformedFHEMModuleDescriptionJsonException(String function, String folder) { 
		 super("Content of FHEM module description for function '"+function+"' missing. Look at '"+folder+"/"+function+".json'");
	 }
	 
	 
}
