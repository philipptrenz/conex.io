package mapping;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import interfaces.RequestHandlerInterface;
import io.swagger.model.Device;
import io.swagger.model.Filter;
import io.swagger.model.Function;
import mapping.read.Jsonlist2Parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class Main {
	
	
	/*
	 * For testing purposes
	 */
	public static void main(String [] args){
		
		Jsonlist2Parser parser = new Jsonlist2Parser();
		ArrayList<Device> devices = parser.parse();
		
		System.out.println("# mapped devices: "+devices.size());
		
		/*
		 *  TODO: 
		 *  
		 *  1. Sort out with filter object
		 *  2. return devices or retrieve ids (function, room, group) and return
		 */
	}

}
