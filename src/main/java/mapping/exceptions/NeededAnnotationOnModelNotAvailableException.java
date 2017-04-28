package mapping.exceptions;

public class NeededAnnotationOnModelNotAvailableException extends Exception {
	
	public NeededAnnotationOnModelNotAvailableException(Class modelClass) {
		super("Needed annotation for value mapping at model class "+modelClass.getName()+" missing.");
	}
	
}
