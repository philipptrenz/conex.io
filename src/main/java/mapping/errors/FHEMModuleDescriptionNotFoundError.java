package mapping.errors;

import org.springframework.ui.context.Theme;

public class FHEMModuleDescriptionNotFoundError extends Error {

	public FHEMModuleDescriptionNotFoundError(String fhemDeviceName) {
		super("For '"+fhemDeviceName+"' no matching FHEM module description could be found.");
	}
}
