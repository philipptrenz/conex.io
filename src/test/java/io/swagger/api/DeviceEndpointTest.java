package io.swagger.api;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import io.swagger.Swagger2SpringBoot;
import io.swagger.api.calc.DeviceCalc;
import io.swagger.model.Device;
import io.swagger.model.Devices;
import io.swagger.model.Filter;
import io.swagger.model.Function;
/**
 * @author Timo Schwan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Swagger2SpringBoot.class)
@WebAppConfiguration
public class DeviceEndpointTest {


    private MediaType contentType = MediaType.APPLICATION_JSON;

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Device device;

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Kein passendes Gerät gefunden -> Gibt alle Geräte zurück
     * @throws Exception
     */
    @Test
    public void noMatchFound() throws Exception {
    	Filter f = new Filter();
    	f.setDeviceIds(Arrays.asList("", ""));
    	f.setRoomIds(Arrays.asList("", ""));
    	
        MvcResult result =mockMvc.perform(post("/devices")
                			.content(this.json(f))
                			.contentType(contentType))
                			.andExpect(status().isNotFound())
                			.andReturn();
        result.getResponse().getContentAsString();
        
        Devices devices = this.restTemplate.getForObject("localhost:8080/v0/devices", Devices.class);
        for(Device d : devices.getDevices()) {
        	//if(filter.getDeviceIDs.contains(d.getDeviceId())) {
        }
    }

    /**
     * Gibt ein Gerät anhand aller Filterattribute zurück
     * Status [Complete]
     * @throws Exception
     */
    @Test
    public void getDeviceByAllFilters() throws Exception {
    	List <String> searchDevices = Arrays.asList("testdevice_0");
    	List <String> searchFunctions = Arrays.asList("testfunction_0");
    	List <String> searchGroups = Arrays.asList("testgroup_0");
    	List <String> searchRooms = Arrays.asList("testroom_0");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevices);
    	f.setFunctionIds(searchFunctions);
    	f.setGroupIds(searchGroups);
    	f.setRoomIds(searchRooms);
        
        //TODO Auch ein Header lässt sich im HttpEntity mit einbinden
        HttpEntity<Filter> request = new HttpEntity<>(new Filter());
        ResponseEntity<Devices> response = restTemplate
        		.exchange("localhost:8080/v0/devices", HttpMethod.POST, request, Devices.class);
        
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        
        Devices geraete = response.getBody();
        for (Device d : geraete.getDevices()) {
        assertTrue(DeviceCalc.isDeviceMatchingFiltering(d, f));
        }
    }

	@Test
    public void getDevicesByDevice() throws Exception {
    	List <String> searchDevice = Arrays.asList("");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(searchDevice);
    	
    	
        mockMvc.perform(post("/devices")
        		.content(this.json(f))
        		.contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                
                //Defined for only 1 Device -> Iterator ?
                //hasItems testing...
                .andExpect(jsonPath("$.device[1].device_Ids", hasItems(searchDevice)));
    }

    @Test
    public void readDevices() throws Exception {
        mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}