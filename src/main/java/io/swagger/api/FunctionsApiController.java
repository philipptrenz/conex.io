package io.swagger.api;

import io.swagger.model.Devices;
import io.swagger.model.Error;
import io.swagger.model.Filter;
import io.swagger.model.Ids;
import mapping.AutomationServerConnector;
import io.swagger.annotations.*;
import io.swagger.api.calc.DeviceCalc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-03-22T14:12:09.728Z")

@Controller
public class FunctionsApiController implements FunctionsApi {

	@Autowired
	private AutomationServerConnector connector;

    public ResponseEntity<Ids> functionsPost(@ApiParam(value = "The user specified filter" ,required=true ) @RequestBody Filter filter) {
        DeviceCalc calc = new DeviceCalc(filter, connector.getDevices());
        Ids id = new Ids();
        id.setIds(calc.getFuntionsByDevicesFiltered());
        return new ResponseEntity<Ids>(id, HttpStatus.OK);
    }

}
