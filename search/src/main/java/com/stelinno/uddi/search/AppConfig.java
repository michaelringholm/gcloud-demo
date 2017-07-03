package com.stelinno.uddi.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.stelinno.uddi.search")
public class AppConfig {
	
	@Bean
	String SEARCH_INDEX() {
		return "PrimaryUDDISearchIndex";
	}
	
	@Bean
	IndexHelper indexHelper() {
		return new IndexHelper();
	}
	
	@Bean
	String version() {
		System.out.println("called version()!");
		return "V1.0.201707031344";
	}
}
