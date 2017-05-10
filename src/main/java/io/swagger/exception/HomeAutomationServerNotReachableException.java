package io.swagger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.SERVICE_UNAVAILABLE, reason="The home automation server is not reachable")
public class HomeAutomationServerNotReachableException extends Exception {
	
	public HomeAutomationServerNotReachableException() {
		super();
	}
	
	public HomeAutomationServerNotReachableException(String arg0) {
		super(arg0);
	}

}
