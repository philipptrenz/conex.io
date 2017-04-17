package io.swagger.api;

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
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
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

    private String userName = "bdussault";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Device device;

    private List<Device> bookmarkList = new ArrayList<>();

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

    /*@Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.bookmarkRepository.deleteAllInBatch();
        this.accountRepository.deleteAllInBatch();

        this.account = accountRepository.save(new Account(userName, "password"));
        this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/1/" + userName, "A description")));
        this.bookmarkList.add(bookmarkRepository.save(new Bookmark(account, "http://bookmark.com/2/" + userName, "A description")));
    }*/

    /**
     * Kein passendes Gerät gefunden -> Gibt alle Geräte zurück
     * @throws Exception
     */
    @Test
    public void noMatchFound() throws Exception {
        mockMvc.perform(post("/devices")
                .content(this.json(new Filter()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    /**
     * Gibt ein Gerät anhand aller Filterattribute zurück
     * @throws Exception
     */
    @Test
    public void getDeviceByAllFitlers() throws Exception {
    	Filter f = new Filter();
    	f.setDeviceIds(Arrays.asList("", ""));
    	f.setFunctionIds(Arrays.asList("", ""));
    	f.setGroupIds(Arrays.asList("", ""));
    	f.setRoomIds(Arrays.asList("", ""));
    	
    	
        mockMvc.perform(post("/devices")
        		.content(this.json(f))
        		.contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.device_Ids", is(f.getDeviceIds())))
                .andExpect(jsonPath("$.room_ids", is(f.getRoomIds())))
        		.andExpect(jsonPath("$.group_ids", is(f.getFunctionIds())))
                .andExpect(jsonPath("$.function_ids", is(f.getFunctionIds())));
    }

    @Test
    public void readBookmarks() throws Exception {
        mockMvc.perform(get("/" + userName + "/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
               // .andExpect(jsonPath("$", hasSize(2)))
               // .andExpect(jsonPath("$[0].id", is(this.bookmarkList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].uri", is("http://bookmark.com/1/" + userName)))
                .andExpect(jsonPath("$[0].description", is("A description")))
               // .andExpect(jsonPath("$[1].id", is(this.bookmarkList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" + userName)))
                .andExpect(jsonPath("$[1].description", is("A description")));
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