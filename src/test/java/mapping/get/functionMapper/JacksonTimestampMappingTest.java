package mapping.get.functionMapper;

import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import io.swagger.Swagger2SpringBoot;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Swagger2SpringBoot.class)
public class JacksonTimestampMappingTest {
	
	@Autowired
	ApplicationContext context;

    private static final String json = "{ \n" +
            "  \"timestamp\" : \"2014-08-20T11:51:31.233Z\" \n" +
            "}";

    @Test
    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
class GreetingResource {
    @JsonProperty("timestamp")
    private DateTime date;

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GreetingResource{" +
                "date=" + date +
                '}';
    }
}