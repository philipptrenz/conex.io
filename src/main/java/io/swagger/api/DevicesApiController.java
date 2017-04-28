package io.swagger.api;

import io.swagger.model.Devices;
import io.swagger.model.Filter;
import io.swagger.model.Function;
import io.swagger.model.OnOff;
import io.swagger.model.Patcher;
import mapping.AutomationServerConnector;
import io.swagger.annotations.*;
import io.swagger.api.calc.DeviceCalc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Controller
public class DevicesApiController implements DevicesApi {

	@Autowired
	private AutomationServerConnector connector;
	
    public ResponseEntity<Void> devicesPatch(@ApiParam(value = "Filter object with function values" ,required=true ) @RequestBody Patcher patcher) {
    	DeviceCalc calc = new DeviceCalc(patcher.getFilter(), connector.getDevices());
        Devices list = new Devices();
        list.setDevices(calc.getDeviceListFilteringWithPatcherFunction(patcher.getFunction()));
        System.out.println(list.getDevices());
        connector.setDevices(list.getDevices(), patcher.getFunction());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Devices> devicesPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter) {
        DeviceCalc calc = new DeviceCalc(filter, connector.getDevices());
        Devices list = new Devices();
        list.setDevices(calc.getDeviceListFiltered());
    	return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
