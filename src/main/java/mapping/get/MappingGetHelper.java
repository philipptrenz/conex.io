package mapping.get;

import com.fasterxml.jackson.databind.JsonNode;

import mapping.exceptions.NoValidKeyPathException;

public class MappingGetHelper {
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
}
