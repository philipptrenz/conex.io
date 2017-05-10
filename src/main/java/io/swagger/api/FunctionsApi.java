package io.swagger.api;

import io.swagger.model.Error;
import io.swagger.model.Filter;
import io.swagger.model.Ids;

import io.swagger.annotations.*;
import io.swagger.exception.HomeAutomationServerNotReachableException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Api(value = "functions", description = "the functions API")
public interface FunctionsApi {

    @ApiOperation(value = "", notes = "Get a list of function ID's. ", response = Ids.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "List of function ID's", response = Ids.class),
        @ApiResponse(code = 200, message = "Unexpected error", response = Ids.class) })
    @RequestMapping(value = "/functions",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Ids> functionsPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter)  throws HomeAutomationServerNotReachableException;

}
