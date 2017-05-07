package mapping;

import java.lang.reflect.Method;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import mapping.exceptions.NeededAnnotationOnModelNotAvailableException;
import mapping.exceptions.NoValidKeyPathException;

public class MappingHelper {
	
	private final static Logger log = LoggerFactory.getLogger(MappingHelper.class);
	
	public static JsonNode navigateJsonKeyPath(JsonNode node, String path) throws NoValidKeyPathException {
		// remove whitespaces, linebreaks etc
		path = path.replaceAll("\\r\\n|\\r|\\n", "").replace(" ", "");
		String[] keys = path.split("/");
		
		JsonNode temp = node;
		for (String key : keys) {			
			if (!temp.has(key)) throw new NoValidKeyPathException();
			temp = temp.get(key);
		}
		return temp;
	}
	
	public static int getConstraintValueFromFunctionClassAnnotation(String type, String propertyName, Object function) {
		try {
			Method[] methods = function.getClass().getMethods();
			Method setMethod = null;
			for (Method m : methods) {
				
				if (m.getName().toLowerCase().contains("get"+propertyName.toLowerCase())) {
					setMethod = m;
					
					if (type.equals("min")) {
						if (setMethod.getAnnotation(Min.class) == null) throw new NeededAnnotationOnModelNotAvailableException(function.getClass());
						return (int) setMethod.getAnnotation(Min.class).value();
					} else if (type.equals("max")) {
						if (setMethod.getAnnotation(Max.class) == null) throw new NeededAnnotationOnModelNotAvailableException(function.getClass());
						return (int) setMethod.getAnnotation(Max.class).value();
					} else {
						log.error("Function 'getConstraintValueFromFunctionClassAnnotation' has no case for type '"+type+"'");
					}
				}
			}
		} catch (Exception e) {
			log.error("Retrieving min/max value from Function class notation failed", e);
		}
		return 0;
	}
}
