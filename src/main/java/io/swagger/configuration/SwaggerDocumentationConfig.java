package io.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Configuration
public class SwaggerDocumentationConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("conex.io REST-Like API")
            .description("The conex.io API provides the functionality to interact with home automation devices, which are connected to a home automation server, detached from the manufacturer specific communication syntax.  ## Overview 200. Conventions * Example Requests & Responses * Shema * HTTP Verbs * Errors * Miscellaneous  ### 1. Conventions #### Naming: * Parameters with the ending _ids are always strings * Standalone names are objects  #### Filter: The parameters in the filter object:  * device_ids - Unique device identifiers * room_ids - Unique room identifiers * group_ids - Unqiue group identifiers * function_ids - Unique function identifiers  The requested IDs are placed in an array of strings in the filter object as parameters. If a parameter shall not be set, the value has to be set to null instead of being omitted.  ### 2. Example Requests & Responses The developer has to make sure that the specified parameters in the filter are logical.  #### Bad Example: ```   POST /groups   {     ids:null,    rooms:null,    groups:lampen,    function:null,   }   Response:   {     group_ids:[lampen]   } ```  ### 3. Schema  Parameters are always sent and received as strings, expect through the devices endpoint where you get each device as an object with all the information attached.  All data is sent and received as JSON.  ### 4. HTTP Methods  Implemented HTTP methods for the API are:  * POST - Retrieve resources * PATCH - Update resources  ### 5. Errors  Sending an invalid JSON will result in a `400 Bad Request`: ```   HTTP/1.1 400 Bad Request      Error    {     code: 400     message: Problem parsing JSON   }    ```  Sending the wrong type of JSON result in a `400 Bad Request`: ```   HTTP/1.1 400 Bad Request      Error    {     code: 400     message: Body should be a valid JSON object   }    ```  #### Additional Error Codes:  * 204:   description: No Content    * 304:   description: Not Modified    * 400:   description: Bad request    * 401:   description: Unauthorized    * 403:    description: Forbidden     * 404:    description: Not Found     * 405:    description: Method not Allowed     * 415:    description: Unsupported Media Type     * 500:   description: Internal Server Error    * 503:   description: Service Unavailable  ### 6. Miscellaneous  #### Considerations: * Authentication  * Timezones  * Timestamps in ISO 8601 format: ```   YYYY-MM-DDTHH:MM:SSZ ```  * Term of Use  * Rate Limiting     * Limit requests per IP or time    * Too many requests will result in a ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â´503ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â´ error code (server unavailable) * How To/ Tutorial  * API Version and MIME Types  * Changelog ")
            .license("")
            .licenseUrl("http://unlicense.org")
            .termsOfServiceUrl("")
            .version("0.9.4")
            .contact(new Contact("","", ""))
            .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("io.swagger.api"))
                    .build()
                .directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }

}
