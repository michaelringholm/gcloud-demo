package com.stelinno.uddi.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.stelinno.uddi.search")
public class AppConfig {
	@Bean
	IndexHelper indexHelper() {
		return new IndexHelper();
	}
	
	@Bean
	String version() {
		return "V1.0.201707031344";
	}
}
