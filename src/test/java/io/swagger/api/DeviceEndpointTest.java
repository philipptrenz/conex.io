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
import io.swagger.model.Function;
import io.swagger.model.Patcher;


/**
 * device endpoint testing cases.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Swagger2SpringBoot.class)
@WebAppConfiguration
public class DeviceEndpointTest {
    
    /** The rest template. */
    //@Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();
    
    /** The api device endpoint url. */
    private final String apiDeviceEndpoint = "http://localhost:8080/v0/devices";

    
    /**
     * Check device endpoint by non-permitted http-methods.
     *
     * @throws Exception the exception
     */
    @Test
    public void devicesEndpointNotPermittedHttpMethod() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
    	filter.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Devices> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.GET, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Devices> responsePut = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.PUT, request, Devices.class);
        
        assertThat(responsePut.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Devices> responseDelete = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.DELETE, request, Devices.class);
        
        assertThat(responseDelete.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
        
        ResponseEntity<Devices> responseHead = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.HEAD, request, Devices.class);
        
        assertThat(responseHead.getStatusCode(), is(HttpStatus.METHOD_NOT_ALLOWED));
    }
    
    /**
     * Get devices by non-json request.
     *
     * @throws Exception the exception
     */
    @Test
    public void devicesPostEndpointNonJsonRequest() throws Exception {
        
        HttpEntity <String> request = new HttpEntity<String>("");
        ResponseEntity<Devices> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.UNSUPPORTED_MEDIA_TYPE));
    }
    
    
    
    /**
     * Gets devices by all filters.
     *
     * @throws Exception the exception
     */
    @Test
    public void postDevicesByAllFilters() throws Exception {
    	List <String> searchDevices = Arrays.asList("testdevice_21");
    	List <String> searchFunctions = Arrays.asList("testfunction_1");
    	List <String> searchGroups = Arrays.asList("testgroup_11");
    	List <String> searchRooms = Arrays.asList("testroom_10");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	filter.setFunctionIds(searchFunctions);
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Devices> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices geraete = response.getBody();
        for (Device device : geraete.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(device, filter));
        }
    }
    
    /**
     * Gets devices by device only filtering.
     *
     * @throws Exception the exception
     */
    @Test
    public void postDevicesByDeviceFilter() throws Exception {
    	List <String> searchDevices = Arrays.asList("testdevice_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Devices> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices deviceListResponse = response.getBody();
        for (Device device : deviceListResponse.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(device, filter));
        }
    }
    
    /**
     * Gets devices by function filter only.
     *
     * @throws Exception the exception
     */
    @Test
    public void postDevicesByFunctionFilter() throws Exception {
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	
    	Filter filter = new Filter();
    	
    	filter.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Devices> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices deviceListResponse = response.getBody();
        for (Device device : deviceListResponse.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(device, filter));
        }
    }
    
    /**
     * Gets devices by group filter only.
     *
     * @throws Exception the exception
     */
    @Test
    public void postDevicesByGroupFilter() throws Exception {
    	List <String> searchGroups = Arrays.asList("testgroup_11", "testgroup_12");
    	
    	Filter filter = new Filter();
    	
    	filter.setGroupIds(searchGroups);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Devices> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices deviceListResponse = response.getBody();
        for (Device device : deviceListResponse.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(device, filter));
        }
    }
    
    /**
     * Gets devices by room filter only.
     *
     * @throws Exception the exception
     */
    @Test
    public void postDevicesByRoomFilter() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_10", "testroom_12");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Devices> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices deviceListResponse = response.getBody();
        for (Device device : deviceListResponse.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(device, filter));
        }
    }
    
    /**
     * Gets devices by multiple filters.
     *
     * @throws Exception the exception
     */
    @Test
    public void postDevicesByMultipleFilters() throws Exception {
    	List <String> searchRooms = Arrays.asList("testroom_10");
    	List <String> searchFunctions = Arrays.asList("testfunction_1");
    	
    	Filter filter = new Filter();
    	
    	filter.setRoomIds(searchRooms);
    	filter.setFunctionIds(searchFunctions);
        
        HttpEntity<Filter> request = new HttpEntity<Filter>(filter);
        ResponseEntity<Devices> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices deviceListResponse = response.getBody();
        for (Device device : deviceListResponse.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(device, filter));
        }
    }
    
    /**
     * Patch devices by function only.
     *
     * @throws Exception the exception
     */
    @Test
    public void patchDevicesByFunctionOnly() throws Exception {
    	Function filter = new Function();
    	filter.setFunctionId("testfunction_0");
    	
    	Patcher patcher = new Patcher();
    	patcher.setFunction(filter);
        
        HttpEntity<Patcher> request = new HttpEntity<Patcher>(patcher);
        ResponseEntity<Void> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.PATCH, request, Void.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }
    
    /**
     * Patch devices by all filters.
     *
     * @throws Exception the exception
     */
    @Test
    public void patchDevicesByAllFilters() throws Exception {
    	Function function = new Function();
    	function.setFunctionId("onoff");
    	
    	List <String> searchDevices = Arrays.asList("testdevice_21");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	List <String> searchGroups = Arrays.asList("testgroup_11");
    	List <String> searchRooms = Arrays.asList("testroom_12");
    	
    	Filter filter = new Filter();
    	
    	filter.setDeviceIds(searchDevices);
    	filter.setFunctionIds(searchFunctions);
    	filter.setGroupIds(searchGroups);
    	filter.setRoomIds(searchRooms);
    	
    	Patcher patcher = new Patcher();
    	patcher.setFunction(function);
    	patcher.setFilter(filter);
        
        HttpEntity<Patcher> request = new HttpEntity<Patcher>(patcher);
        ResponseEntity<Void> response = restTemplate
        		.exchange(apiDeviceEndpoint, HttpMethod.PATCH, request, Void.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }
}