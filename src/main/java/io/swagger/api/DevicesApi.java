package io.swagger.api;

import io.swagger.model.Devices;
import io.swagger.model.Filter;
import io.swagger.model.Patcher;

import io.swagger.annotations.*;
import io.swagger.exception.HomeAutomationServerNotReachableException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Interface DevicesApi.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Api(value = "devices", description = "the devices API")
public interface DevicesApi {

    /**
     * Devices patch.
     *
     * @param patcher the patcher
     * @return the response entity
     * @throws HomeAutomationServerNotReachableException the home automation server not reachable exception
     */
    @ApiOperation(value = "", notes = "Modifies the state of a single `device` or group of `devices`. ", response = Void.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Void.class),
        @ApiResponse(code = 200, message = "Unexpected error", response = Void.class) })
    @RequestMapping(value = "/devices",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity<Void> devicesPatch(@ApiParam(value = "Filter object with function values" ,required=true ) @RequestBody Patcher patcher)  throws HomeAutomationServerNotReachableException;


    /**
     * Devices post.
     *
     * @param filter the filter
     * @return the response entity
     * @throws HomeAutomationServerNotReachableException the home automation server not reachable exception
     */
    @ApiOperation(value = "", notes = "Get a list of `device` objects based on the specified filter. ", response = Devices.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "The device objects", response = Devices.class),
        @ApiResponse(code = 200, message = "Unexpected error", response = Devices.class) })
    @RequestMapping(value = "/devices",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Devices> devicesPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter)  throws HomeAutomationServerNotReachableException;

}
