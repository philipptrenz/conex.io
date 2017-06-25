package mapping;

import java.lang.reflect.Method;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import mapping.exceptions.NeededAnnotationOnModelNotAvailableException;
import mapping.exceptions.NoValidKeyPathException;

/**
 * The Class MappingHelper.
 * 
 * This class provides several methods needed for mapping.
 * 
 * @author Philipp Trenz
 */
public class MappingHelper {
	
	/** The Constant log. */
	private final static Logger log = LoggerFactory.getLogger(MappingHelper.class);
	
	/** The Constant MODEL_PACKAGE where models get generated to. */
	private final static String MODEL_PACKAGE = "io.swagger.model";
	
	/**
	 * Navigate json key path.
	 *
	 * @param node the node
	 * @param path the path to the value
	 * @return the json node
	 * @throws NoValidKeyPathException the no valid key path exception
	 */
	public static JsonNode navigateJsonKeyPath(JsonNode node, String path) throws NoValidKeyPathException {
		// remove whitespaces, linebreaks etc
		path = path.replaceAll("\\r\\n|\\r|\\n", "").replace(" ", "");
		String[] keys = path.split("/");
		
		JsonNode temp = node;
		for (String key : keys) {			
			if (!temp.has(key)) {
				//if (!path.endsWith("room") && !path.endsWith("group")) log.error("NoValidKeyPathException:"+"\npath: "+path+"\nNode :"+node);
				throw new NoValidKeyPathException();
			}
			temp = temp.get(key);
		}
		return temp;
	}
	
	/**
	 * Gets the constraint value from function class annotation.
	 *
	 * @param type the type
	 * @param propertyName the property name
	 * @param function the function
	 * @return the constraint value from function class annotation
	 */
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
	
	/**
	 * Find function class by function id.
	 *
	 * @param functionId the function id
	 * @return the class
	 */
	public static Class<?> findFunctionClassByFunctionId(String functionId) {
		try{
			return Class.forName(MODEL_PACKAGE + "." + functionId);
		} catch (ClassNotFoundException e){
			log.error("Function class '"+functionId+"' could not be found!", e);
			return null;
		}
	}
		
	
}
