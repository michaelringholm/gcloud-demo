package com.stelinno.uddi.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

	@Autowired
	private String version;
	
	@RequestMapping(value="/version", method=RequestMethod.GET)
	public String version() {
		return version;
	}
}
