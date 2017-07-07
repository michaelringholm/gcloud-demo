package com.stelinno.uddi.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.gson.Gson;

@Configuration
@ComponentScan("com.stelinno.uddi.search")
public class AppConfig {
	
	@Bean String SEARCH_OPTION_CONTROLLER_SEARCH_INDEX() {
		return "UDDI_PRIMARY_SEARCH_INDEX";
	}
	
	@Bean String SCHEMA_CONTROLLER_SEARCH_INDEX() {
		return "UDDI_PRIMARY_SEARCH_INDEX";
	}
	
	@Bean String SEARCH_CONTROLLER_SEARCH_INDEX() {
		return "UDDI_PRIMARY_SEARCH_INDEX";
	}
	
	@Bean String DELETE_CONTROLLER_SEARCH_INDEX() {
		return "UDDI_PRIMARY_SEARCH_INDEX";
	}
	
	@Bean String INDEX_CONTROLLER_SEARCH_INDEX() {
		return "UDDI_PRIMARY_SEARCH_INDEX";
	}
	
	@Bean String SEARCH_INDEX_CONTROLLER_INDEX_NAME() {
		return "UDDI_PRIMARY_SEARCH_INDEX";
	}
	
	@Bean Gson gson() {
		return new Gson();
	}
	
	@Bean HttpHeaders jsonHttpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}	
	
	@Bean IndexHelper indexHelper() {
		return new IndexHelper();
	}
	
	@Bean String version() {
		System.out.println("called version()!");
		return "V1.0.2017-07-06-10:31";
	}
}
