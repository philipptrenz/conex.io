package io.swagger.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to swagger api documentation 
 */
@Controller
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/")
	public String index() {
		log.info("Request for swagger-ui.html");
		return "redirect:swagger-ui.html";
	}
}
