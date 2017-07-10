package io.swagger.api;

import io.swagger.model.Devices;
import io.swagger.model.Filter;
import io.swagger.model.Patcher;
import io.swagger.HomeAutomationServerConnector;
import io.swagger.annotations.*;
import io.swagger.api.calc.DeviceCalc;
import io.swagger.exception.HomeAutomationServerNotReachableException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * The Class DevicesApiController.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Controller
public class DevicesApiController implements DevicesApi {

	/** The connector. */
	@Autowired
	private HomeAutomationServerConnector connector;
	
    /* (non-Javadoc)
     * @see io.swagger.api.DevicesApi#devicesPatch(io.swagger.model.Patcher)
     */
    public ResponseEntity<Void> devicesPatch(@ApiParam(value = "Filter object with function values" ,required=true ) @RequestBody Patcher patcher) throws HomeAutomationServerNotReachableException{
    	if(patcher.getFilter() != null && patcher.getFunction() != null) {
    		DeviceCalc calc = new DeviceCalc(patcher.getFilter(), connector.getDevices());
        Devices list = new Devices();
        list.setDevices(calc.getDeviceListFilteringWithPatcherFunction(patcher.getFunction()));
        connector.setDevices(list.getDevices(), patcher.getFunction());
        return new ResponseEntity<Void>(HttpStatus.OK);
    	}
    	else {
    		return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    	}
    }

    /* (non-Javadoc)
     * @see io.swagger.api.DevicesApi#devicesPost(io.swagger.model.Filter)
     */
    public ResponseEntity<Devices> devicesPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter)  throws HomeAutomationServerNotReachableException {
        DeviceCalc calc = new DeviceCalc(filter, connector.getDevices());
        Devices list = new Devices();
        list.setDevices(calc.getDeviceListFiltered());
    	return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
