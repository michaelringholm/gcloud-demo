package com.stelinno.uddi.search.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.stelinno.uddi.search.IndexHelper;

@Configuration
@ComponentScan("com.stelinno.uddi.search")
public class AppConfig {
	
	@Bean String SEARCH_OPTION_CONTROLLER_SEARCH_INDEX() {
		return "SEARCH_OPTION_CONTROLLER_SEARCH_INDEX";
	}
	
	@Bean String SCHEMA_CONTROLLER_SEARCH_INDEX() {
		return "SCHEMA_CONTROLLER_SEARCH_INDEX";
	}
	
	@Bean String SEARCH_CONTROLLER_SEARCH_INDEX() {
		return "SEARCH_CONTROLLER_SEARCH_INDEX";
	}
	
	@Bean String DELETE_CONTROLLER_SEARCH_INDEX() {
		return "DELETE_CONTROLLER_SEARCH_INDEX";
	}
	
	@Bean String INDEX_CONTROLLER_SEARCH_INDEX() {
		return "INDEX_CONTROLLER_SEARCH_INDEX";
	}
	
	@Bean String SEARCH_INDEX_CONTROLLER_INDEX_NAME() {
		return "UDDI_PRIMARY_SEARCH_INDEX";
	}
	
	@Bean
	IndexHelper indexHelper() {
		//return new UnitTestIndexHelper();
		return new IndexHelper();
	}
	
	@Bean
	String version() {
		return "V1.0.201707031344";
	}
}
