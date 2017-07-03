package com.stelinno.uddi.search.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.stelinno.uddi.search.IndexHelper;

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
