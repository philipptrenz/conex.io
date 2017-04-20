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
import io.swagger.api.calc.DeviceCalc;
import io.swagger.model.Device;
import io.swagger.model.Devices;
import io.swagger.model.Filter;
/**
 * @author Timo Schwan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Swagger2SpringBoot.class)
@WebAppConfiguration
public class DeviceEndpointTest {
    
    //@Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();

    
    @Test
    public void devicesEndpointNotPermittedHttpMethod() throws Exception {
    	List <String> searchRooms = Arrays.asList("EG.Wohnzimmer");
    	List <String> searchFunctions = Arrays.asList("onoff");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
    	f.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Devices> response = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.GET, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Devices> responsePut = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.PUT, request, Devices.class);
        
        assertThat(responsePut.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Devices> responseDelete = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.DELETE, request, Devices.class);
        
        assertThat(responseDelete.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Devices> responseHead = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.HEAD, request, Devices.class);
        
        assertThat(responseHead.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
    }
    
    @Test
    public void devicesEndpointNonJsonRequest() throws Exception {
        
        HttpEntity <String> request = new HttpEntity<String>("");
        ResponseEntity<Devices> response = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
    }
    
    
    
    /**
     * Gibt ein Gerät anhand aller Filterattribute zurück
     * Status [Complete]
     * @throws Exception
     */
    @Test
    public void getDeviceByAllFilters() throws Exception {
    	List <String> searchDevices = Arrays.asList("");
    	List <String> searchFunctions = Arrays.asList("onoff");
    	List <String> searchGroups = Arrays.asList("");
    	List <String> searchRooms = Arrays.asList("");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
    	System.out.println(f.toString());
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Devices> response = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.POST, request, Devices.class);
        System.out.println(response);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices geraete = response.getBody();
        for (Device d : geraete.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(d, f));
        }
    }
    
    @Test
    public void getDeviceByDeviceFilter() throws Exception {
    	List <String> searchDevices = Arrays.asList("wz_rauchmelder");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Devices> response = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices geraete = response.getBody();
        for (Device d : geraete.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(d, f));
        }
    }
    
    @Test
    public void getDeviceByFunctionFilter() throws Exception {
    	List <String> searchFunctions = Arrays.asList("onoff");
    	
    	Filter f = new Filter();
    	
    	f.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Devices> response = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices geraete = response.getBody();
        for (Device d : geraete.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(d, f));
        }
    }
    
    @Test
    public void getDeviceByGroupFilter() throws Exception {
    	List <String> searchGroups = Arrays.asList("Schalter", "Handsender");
    	
    	Filter f = new Filter();
    	
    	f.setGroupIds(searchGroups);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Devices> response = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices geraete = response.getBody();
        for (Device d : geraete.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(d, f));
        }
    }
    
    @Test
    public void getDeviceByRoomFilter() throws Exception {
    	List <String> searchRooms = Arrays.asList("EG.Wohnzimmer");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Devices> response = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices geraete = response.getBody();
        for (Device d : geraete.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(d, f));
        }
    }
    @Test
    public void getDeviceByMultipleFilters() throws Exception {
    	List <String> searchRooms = Arrays.asList("EG.Wohnzimmer");
    	List <String> searchFunctions = Arrays.asList("onoff");
    	
    	Filter f = new Filter();
    	
    	f.setRoomIds(searchRooms);
    	f.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(f);
        ResponseEntity<Devices> response = restTemplate
        		.exchange("http://localhost:8080/v0/devices", HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices geraete = response.getBody();
        for (Device d : geraete.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(d, f));
        }
    }
}