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
    
    private String url = "http://localhost:8080/v0/groups";

    
    @Test
    public void groupsEndpointNotPermittedHttpMethod() throws Exception {
    	List <String> searchRooms = Arrays.asList("EG.Wohnzimmer");
    	List <String> searchFunctions = Arrays.asList("onoff");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
    	f.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(url, HttpMethod.GET, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responsePut = restTemplate
        		.exchange(url, HttpMethod.PUT, request, Ids.class);
        
        assertThat(responsePut.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responsePatch = restTemplate
        		.exchange(url, HttpMethod.PATCH, request, Ids.class);
        
        assertThat(responsePatch.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responseDelete = restTemplate
        		.exchange(url, HttpMethod.DELETE, request, Ids.class);
        
        assertThat(responseDelete.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Ids> responseHead = restTemplate
        		.exchange(url, HttpMethod.HEAD, request, Ids.class);
        
        assertThat(responseHead.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
    }
    
    @Test
    public void groupsEndpointNonJsonRequest() throws Exception {
        
        HttpEntity <String> request = new HttpEntity<String>("");
        ResponseEntity<Ids> response = restTemplate
        		.exchange(url, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
    }
    
    
    
    /**
     * Gibt ein Gerät anhand aller Filterattribute zurück
     * Status [Complete]
     * @throws Exception
     */
    @Test
    public void getGroupsByAllFilters() throws Exception {
    	List <String> searchDevices = Arrays.asList("dg.jz.deckenleuchte");
    	List <String> searchFunctions = Arrays.asList("onoff");
    	List <String> searchGroups = Arrays.asList("Schalter");
    	List <String> searchRooms = Arrays.asList("DG.Jolina");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(url, HttpMethod.POST, request, Ids.class);
        
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("Schalter"));
    }
    
    @Test
    public void getGroupsByDeviceFilter() throws Exception {
    	List <String> searchDevices = Arrays.asList("dg.jz.deckenleuchte");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(url, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("Schalter"));
    }
    
    @Test
    public void getGroupsByFunctionFilter() throws Exception {
    	List <String> searchFunctions = Arrays.asList("onoff", "dimmer");
    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(url, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("Schalter"));
        assertTrue(groupIds.getIds().contains("Steckdosen"));
        assertTrue(groupIds.getIds().contains("Handsender"));
    }
    
    @Test
    public void getGroupsByGroupFilter() throws Exception {
    	List <String> searchGroups = Arrays.asList("Handsender");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(url, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("Handsender"));
    }
    
    @Test
    public void getGroupsByRoomFilter() throws Exception {
    	List <String> searchRooms = Arrays.asList("EG.Wohnzimmer");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(url, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids functionIds = response.getBody();
        
        assertTrue(functionIds.getIds().contains("onoff"));
    }
    @Test
    public void getGroupsByMultipleFilters() throws Exception {
    	List <String> searchRooms = Arrays.asList("EG.Wohnzimmer");
    	List <String> searchFunctions = Arrays.asList("onoff");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
    	f.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Ids> response = restTemplate
        		.exchange(url, HttpMethod.POST, request, Ids.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Ids groupIds = response.getBody();
        
        assertTrue(groupIds.getIds().contains("Schalter"));
        assertTrue(groupIds.getIds().contains("Steckdosen"));
        assertTrue(groupIds.getIds().contains("Handsender"));
    }
}