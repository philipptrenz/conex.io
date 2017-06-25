package io.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * The Class SwaggerDocumentationConfig.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Configuration
public class SwaggerDocumentationConfig {

    /**
     * Api info.
     *
     * @return the api info
     */
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("conex.io REST-Like API")
            .description("The conex.io API provides the functionality to interact with home automation devices, which are connected to a home automation server, detached from the manufacturer specific communication syntax.")
            .version("0.9.5")
            .build();
    }

    /**
     * Custom implementation.
     *
     * @return the docket
     */
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
