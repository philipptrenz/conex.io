package mapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FHEMJsonlist2Connector {
	
	public static String getJsonlist2Result() {
		
		// TODO: Implement real network connector to get jsonlist2
		
		return getJsonlist2MockupAsStringFromFile();
	}
	
	
	/*
	 * Just for testing purposes
	 */
	private static String getJsonlist2MockupAsStringFromFile() {
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get("testdata/jsonlist2_20170324.json"));
			return new String(encoded, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}