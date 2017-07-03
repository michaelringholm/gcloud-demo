package com.stelinno.uddi.search.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.stelinno.uddi.search.IndexHelper;

@Configuration
@ComponentScan("com.stelinno.uddi.search")
public class AppConfig {
	
	@Bean
	String SEARCH_INDEX() {
		return "UnitTestUDDISearchIndex";
	}
	
	@Bean
	String SEARCH_OPTION_CONTROLLER_SEARCH_INDEX() {
		return "SEARCH_OPTION_CONTROLLER_SEARCH_INDEX";
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
