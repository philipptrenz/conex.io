/*
 * 
 */
package mapping.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class NeededAnnotationOnModelNotAvailableException.
 */
public class NeededAnnotationOnModelNotAvailableException extends Exception {
	
	/**
	 * Instantiates a new needed annotation on model not available exception.
	 *
	 * @param modelClass the model class
	 */
	public NeededAnnotationOnModelNotAvailableException(Class modelClass) {
		super("Needed annotation for value mapping at model class "+modelClass.getName()+" missing.");
	}
	
}
