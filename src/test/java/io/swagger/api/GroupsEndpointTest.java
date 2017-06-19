package io.swagger.api;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.Filter;
import io.swagger.model.Ids;
/**
 * @author Timo Schwan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Swagger2SpringBoot.class)
@WebAppConfiguration
public class GroupsEndpointTest {
    
    //@Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();
    
    private String apiGroupEndpoint = "http://localhost:8080/v0/groups";

    
    @Test
    public void groupsEndpointNotPermittedHttpMethod() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_11");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
    	filter.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.GET, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responsePut = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.PUT, request, Ids.class);
        
        assertThat(responsePut.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responsePatch = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.PATCH, request, Ids.class);
        
        assertThat(responsePatch.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responseDelete = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.DELETE, request, Ids.class);
        
        assertThat(responseDelete.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responseHead = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.HEAD, request, Ids.class);
        
        assertThat(responseHead.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
    }
    
    @Test
    public void groupsEndpointNonJsonRequest() throws Exception {
        
        HttpEntity <String> request = new HttpEntity<String>("");
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
    }
    
    
    
    /**
     * Gibt ein Gerät anhand aller Filterattribute zurück
     * Status [Complete]
     * @throws Exception
     */
    @Test
    public void getGroupsByAllFilters() throws Exception {
    	List <String> searchDevices = Arrays.asList("testdevice_21");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	List <String> searchGroups = Arrays.asList("testgroup_11");
    	List <String> searchRooms = Arrays.asList("testroom_11");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	filter.setFunctionIds(searchFunctions);
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.POST, request, Ids.class);
        
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("testgroup_10"));
    }
    
    @Test
    public void getGroupsByDeviceFilter() throws Exception {
    	List <String> searchDevices = Arrays.asList("testdevice_21");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("testgroup_12"));
    }
    
    @Test
    public void getGroupsByFunctionFilter() throws Exception {
    	List <String> searchFunctions = Arrays.asList("testfunction_0", "testfunction_1");
    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("testgroup_10"));
        assertTrue(groupIds.getIds().contains("testgroup_11"));
        assertTrue(groupIds.getIds().contains("testgroup_12"));
    }
    
    @Test
    public void getGroupsByGroupFilter() throws Exception {
    	List <String> searchGroups = Arrays.asList("testgroup_11");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("testgroup_11"));
    }
    
    @Test
    public void getGroupsByRoomFilter() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_11");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids functionIds = response.getBody();
        
        assertTrue(functionIds.getIds().contains("testgroup_12"));
    }
    @Test
    public void getGroupsByMultipleFilters() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_12");
    	List <String> searchFunctions = Arrays.asList("testfunction_1");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
    	filter.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiGroupEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("testgroup_10"));
        assertTrue(groupIds.getIds().contains("testgroup_11"));
        assertTrue(groupIds.getIds().contains("testgroup_12"));
    }
}