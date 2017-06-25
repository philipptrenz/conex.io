package io.swagger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class HomeAutomationServerNotReachableException.
 * 
 * @author Philipp Trenz
 */
@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="The home automation server is not reachable")
public class HomeAutomationServerNotReachableException extends Exception {
	
	/**
	 * Instantiates a new home automation server not reachable exception.
	 */
	public HomeAutomationServerNotReachableException() {
		super();
	}
	
	/**
	 * Instantiates a new home automation server not reachable exception.
	 *
	 * @param arg0 the arg 0
	 */
	public HomeAutomationServerNotReachableException(String arg0) {
		super(arg0);
	}

}
