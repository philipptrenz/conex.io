package io.swagger.api;

import io.swagger.model.Error;
import io.swagger.model.Filter;
import io.swagger.model.Ids;

import io.swagger.annotations.*;
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

@Api(value = "groups", description = "the groups API")
public interface GroupsApi {

    @ApiOperation(value = "", notes = "Get a list of group ID's. ", response = Ids.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "List of group ID's", response = Ids.class),
        @ApiResponse(code = 200, message = "Unexpected error", response = Ids.class) })
    @RequestMapping(value = "/groups",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Ids> groupsPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter);

}
