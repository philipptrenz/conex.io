package mapping;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import interfaces.RequestHandlerInterface;
import io.swagger.model.Device;
import io.swagger.model.Filter;
import io.swagger.model.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class Main implements RequestHandlerInterface{

	@Override
	public Device[] getDevices(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRoomIds(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getGroupIds(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getFunctionIds(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean patchDevices(Filter filter, Function function) {
		// TODO Auto-generated method stub
		return false;
	}
	
	// -------------------------------- //
	
	
	/*
	 * For testing purposes
	 */
	public static void main(String [] args){
		
		Jsonlist2Parser parser = new Jsonlist2Parser();
		parser.parse();
	}

}
