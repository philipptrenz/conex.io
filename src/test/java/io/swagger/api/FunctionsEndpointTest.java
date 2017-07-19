package io.swagger.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.Filter;

/**
 * Functions endpoint testing cases.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Swagger2SpringBoot.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class FunctionsEndpointTest {

	/** The api functions endpoint url. */
	private final String apiFunctionsEndpoint = "/functions";

	@Autowired
	/** The WebApplicationContext */
	private WebApplicationContext context;

	/** The MockMvc Testing object */
	private MockMvc mvc;

	/**
	 * Initialize the MockMvc
	 */
	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).alwaysExpect(forwardedUrl(null)).build();

	}

	/**
	 * Parse an object to a json String.
	 * @param obj the Object
	 * @return json-parsed String
	 */
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
     * Check functions endpoint by non-permitted http-methods.
     *
     * @throws Exception the exception
     */
    @Test
    public void functionsEndpointNotPermittedHttpMethod() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter filter = new Filter();
    	
    	
    	filter.setRoomIds(searchRooms);
    	filter.setFunctionIds(searchFunctions);
   
    		/**
    		 * Put-Request-Test
    		 */
			MvcResult result = this.mvc.perform(put(apiFunctionsEndpoint)
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(asJsonString(filter)))
			        .andExpect(status().isMethodNotAllowed())
			        .andReturn();
			
			assertEquals(result.getResponse().getStatus(), HttpStatus.METHOD_NOT_ALLOWED.value());
			
			/**
			 * Get-Request-Test
			 */
			MvcResult result2 = this.mvc.perform(get(apiFunctionsEndpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(filter)))
					.andExpect(status().isMethodNotAllowed())
					.andReturn();

			assertEquals(result2.getResponse().getStatus(), HttpStatus.METHOD_NOT_ALLOWED.value());
			
			/**
			 * Head-Request-Test
			 */
			MvcResult result3 = this.mvc.perform(head(apiFunctionsEndpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(filter)))
					.andExpect(status().isMethodNotAllowed())
					.andReturn();

			assertEquals(result3.getResponse().getStatus(), HttpStatus.METHOD_NOT_ALLOWED.value());

			/**
			 * Delete-Request-Test
			 */
			MvcResult result4 = this.mvc.perform(delete(apiFunctionsEndpoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(filter)))
					.andExpect(status().isMethodNotAllowed())
					.andReturn();

			assertEquals(result4.getResponse().getStatus(), HttpStatus.METHOD_NOT_ALLOWED.value());
    }

	/**
	 * Get functions by non-json request.
	 *
	 * @throws Exception
	 *             the exception
	 */
     @Test
	 public void functionsPostEndpointNonJsonRequest() throws Exception {
	    	List <String> searchRooms = Arrays.asList("testroom_0");
	    	List <String> searchFunctions = Arrays.asList("testfunction_0");
	    	
	    	Filter filter = new Filter();
	    	
	    	
	    	filter.setRoomIds(searchRooms);
	    	filter.setFunctionIds(searchFunctions);

			MvcResult result3 = this.mvc.perform(post(apiFunctionsEndpoint)
					.contentType(MediaType.TEXT_PLAIN)
					.content(asJsonString(filter)))
					.andExpect(status().isUnsupportedMediaType())
					.andReturn();

			assertEquals(result3.getResponse().getStatus(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
			}
}