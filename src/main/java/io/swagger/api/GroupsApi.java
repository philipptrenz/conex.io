package io.swagger.api;

import io.swagger.model.Filter;
import io.swagger.model.Ids;

import io.swagger.annotations.*;
import io.swagger.exception.HomeAutomationServerNotReachableException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Interface GroupsApi.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Api(value = "groups", description = "the groups API")
public interface GroupsApi {

    /**
     * Groups post.
     *
     * @param filter the filter
     * @return the response entity
     * @throws HomeAutomationServerNotReachableException the home automation server not reachable exception
     */
    @ApiOperation(value = "", notes = "Get a list of group ID's. ", response = Ids.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "List of group ID's", response = Ids.class),
        @ApiResponse(code = 200, message = "Unexpected error", response = Ids.class) })
    @RequestMapping(value = "/groups",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Ids> groupsPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter)  throws HomeAutomationServerNotReachableException;

}
