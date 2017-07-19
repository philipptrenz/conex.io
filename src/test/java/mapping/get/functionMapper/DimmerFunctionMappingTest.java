package mapping.get.functionMapper;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import io.swagger.Swagger2SpringBoot;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Swagger2SpringBoot.class)
public class DimmerFunctionMappingTest {
	
	@Autowired
	ApplicationContext context;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private String json = "{\"function_id\":\"dimmer\",\"value\":\"127\",\"timestamp\":\"2016-04-24T16:02:18.000Z\"}";
	
	@Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
    }
}
