package mapping.get.functionMapper;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class DimmerFunctionMappingTest {

	private String json = "{\"function_id\":\"dimmer\",\"value\":\"127\",\"timestamp\":\"2016-04-24T16:02:18.000Z\"}";
	
	@Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());

        System.out.println(mapper.readValue(json, io.swagger.model.Dimmer.class));
    }
}
