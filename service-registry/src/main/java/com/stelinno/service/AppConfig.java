package com.stelinno.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;

@Configuration
@ComponentScan(basePackages={"com.stelinno.service","com.stelinno.service.entities"})
public class AppConfig {
	@Bean 
	public EntityManager entityManager() {
		
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		EntityManagerFactory emf = EntityManagerFactory.getInstance();
		EntityManager em = null;
		
		if(hostName.contains("local"))		
			em = emf.createEntityManager("stelinno-dev", "Stelinno-DEV-f9c6d1e34d94.json", "com.stelinno.uddi");
		else
			em = emf.createDefaultEntityManager("com.stelinno.uddi");
		
		return em;
	}
}