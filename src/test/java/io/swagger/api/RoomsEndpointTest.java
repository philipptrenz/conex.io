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
public class RoomsEndpointTest {
    
    //@Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();
    
    private String apiRoomsEndpoint = "http://localhost:8080/v0/rooms";

    
    @Test
    public void roomsEndpointNotPermittedHttpMethod() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_10");
    	List <String> searchFunctions = Arrays.asList("testfunction_1");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
    	filter.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.GET, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responsePut = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.PUT, request, Ids.class);
        
        assertThat(responsePut.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responsePatch = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.PATCH, request, Ids.class);
        
        assertThat(responsePatch.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responseDelete = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.DELETE, request, Ids.class);
        
        assertThat(responseDelete.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responseHead = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.HEAD, request, Ids.class);
        
        assertThat(responseHead.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
    }
    
    @Test
    public void roomsEndpointNonJsonRequest() throws Exception {
        
        HttpEntity <String> request = new HttpEntity<String>("");
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
    }
    
    
    
    /**
     * Gibt ein Gerät anhand aller Filterattribute zurück
     * Status [Complete]
     * @throws Exception
     */
    @Test
    public void getRoomsByAllFilters() throws Exception {
    	List <String> searchDevices = Arrays.asList("testdevice_21");
    	List <String> searchFunctions = Arrays.asList("testfunction_1");
    	List <String> searchGroups = Arrays.asList("testgroup_11");
    	List <String> searchRooms = Arrays.asList("testroom_12");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.POST, request, Ids.class);
        
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids roomIds = response.getBody();
        
        assertTrue(roomIds.getIds().contains("testroom_11"));
    }
    
    @Test
    public void getRoomsByDeviceFilter() throws Exception {
    	List <String> searchDevices = Arrays.asList("testdevice_21");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids roomIds = response.getBody();
        
        assertTrue(roomIds.getIds().contains("testroom_12"));
    }
    
    @Test
    public void getRoomsByFunctionFilter() throws Exception {
    	List <String> searchFunctions = Arrays.asList("testfunction_0", "testfunction_1");
    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids roomIds = response.getBody();
        
        assertTrue(roomIds.getIds().contains("testroom_10"));
        assertTrue(roomIds.getIds().contains("testroom_11"));
        assertTrue(roomIds.getIds().contains("testroom_12"));
    }
    
    @Test
    public void getRoomsByGroupFilter() throws Exception {
    	List <String> searchGroups = Arrays.asList("testgroup_12");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids roomIds = response.getBody();
        
        assertTrue(roomIds.getIds().contains("testroom_10"));
    }
    
    @Test
    public void getRoomsByRoomFilter() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_11");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids roomIds = response.getBody();
        
        assertTrue(roomIds.getIds().contains("testroom_11"));
    }
    @Test
    public void getRoomsByMultipleFilters() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_10");
    	List <String> searchFunctions = Arrays.asList("testfunction_1");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
    	f.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(apiRoomsEndpoint, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids roomIds = response.getBody();
        
        assertTrue(roomIds.getIds().contains("testroom_10"));
        assertTrue(roomIds.getIds().contains("testroom_11"));
        assertTrue(roomIds.getIds().contains("testroom_12"));
    }
}