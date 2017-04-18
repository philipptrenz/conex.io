package io.swagger.api;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import io.swagger.model.Device;
import io.swagger.model.Filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
/**
 * @author Timo Schwan
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class DeviceControllerTest {


    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Device device;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
    	
        mockMvc.perform(post("/devices")
                .content(this.json(f))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    /**
     * Gibt ein Gerät anhand aller Filterattribute zurück
     * @throws Exception
     */
    @Test
    public void getDeviceByAllFitlers() throws Exception {
    	List <String> sucheDevice = Arrays.asList("");
    	List <String> sucheFunctions = Arrays.asList("");
    	List <String> sucheGroups = Arrays.asList("");
    	List <String> sucheRooms = Arrays.asList("");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(sucheDevice);
    	f.setFunctionIds(sucheFunctions);
    	f.setGroupIds(sucheGroups);
    	f.setRoomIds(sucheRooms);
    	
    	
        mockMvc.perform(post("/devices")
        		.content(this.json(f))
        		.contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                
                //Defined for only 1 Device -> Iterator ?
                .andExpect(jsonPath("$.device[1].device_Ids", hasItems(sucheDevice)))
                .andExpect(jsonPath("$.device[1].room_ids", hasItems(sucheRooms)))
        		.andExpect(jsonPath("$.device[1].group_ids", hasItems(sucheGroups)))
                .andExpect(jsonPath("$.device[1].function_ids", hasItems(sucheFunctions)));
    }
    @Test
    public void getDeviceByDevice() throws Exception {
    	List <String> sucheDevice = Arrays.asList("");
    	
    	Filter f = new Filter();
    	
    	f.setDeviceIds(sucheDevice);
    	
    	
        mockMvc.perform(post("/devices")
        		.content(this.json(f))
        		.contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                
                //Defined for only 1 Device -> Iterator ?
                //hasItems testing...
                .andExpect(jsonPath("$.device[1].device_Ids", hasItems(sucheDevice)));
    }

    @Test
    public void readDevices() throws Exception {
        mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

   /* @Test
    public void createBookmark() throws Exception {
        String bookmarkJson = json(new Bookmark(
                this.account, "http://spring.io", "a bookmark to the best resource for Spring news and information"));

        this.mockMvc.perform(post("/" + userName + "/bookmarks")
                .contentType(contentType)
                .content(bookmarkJson))
                .andExpect(status().isCreated());
    }*/

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}