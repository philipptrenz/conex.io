package mapping.errors;

/**
 * The Class FHEMModuleDescriptionNotFoundError.
 */
public class FHEMModuleDescriptionNotFoundError extends Error {

	/**
	 * Instantiates a new FHEM module description not found error.
	 *
	 * @param fhemDeviceName the fhem device name
	 */
	public FHEMModuleDescriptionNotFoundError(String fhemDeviceName) {
		super("For '"+fhemDeviceName+"' no matching FHEM module description could be found.");
	}
}
