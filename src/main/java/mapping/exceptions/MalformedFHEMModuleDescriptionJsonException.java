package mapping.exceptions;

public class MalformedFHEMModuleDescriptionJsonException extends Exception {
	 public MalformedFHEMModuleDescriptionJsonException() { super(); }
	 public MalformedFHEMModuleDescriptionJsonException(String message) { super(message); }
	 public MalformedFHEMModuleDescriptionJsonException(String message, Throwable cause) { super(message, cause); }
	 public MalformedFHEMModuleDescriptionJsonException(Throwable cause) { super(cause); }
	 
	 
	 public MalformedFHEMModuleDescriptionJsonException(String function, String folder) { 
		 super("Content of FHEM module description for function '"+function+"' missing. Look at '"+folder+"/"+function+".json'");
	 }
	 
	 
}
