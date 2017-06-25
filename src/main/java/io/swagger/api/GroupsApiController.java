package io.swagger.api;

import io.swagger.model.Filter;
import io.swagger.model.Ids;
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
 * The Class GroupsApiController.
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Controller
public class GroupsApiController implements GroupsApi {

	/** The connector. */
	@Autowired
	private HomeAutomationServerConnector connector;

    /* (non-Javadoc)
     * @see io.swagger.api.GroupsApi#groupsPost(io.swagger.model.Filter)
     */
    public ResponseEntity<Ids> groupsPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter)  throws HomeAutomationServerNotReachableException {
        DeviceCalc calc = new DeviceCalc(filter, connector.getDevices());
        Ids id = new Ids();
        id.setIds(calc.getGroupsByDevicesFiltered());
        return new ResponseEntity<Ids>(id, HttpStatus.OK);
    }

}
