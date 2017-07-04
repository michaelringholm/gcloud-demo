package com.stelinno.uddi;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;

@Configuration
@ComponentScan("com.stelinno.uddi")
public class AppConfig {
	
	@Bean 
	public EntityManager entityManager() {
		
		boolean local = new File("force-local.txt").exists();
		Logger.getGlobal().info(String.format("local = %b", local));
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		EntityManagerFactory emf = EntityManagerFactory.getInstance();
		EntityManager em = null;
		
		Logger.getGlobal().info(String.format("hostname = %s", hostName));
		
		if(local)		
			em = emf.createEntityManager("stelinno-dev", "Stelinno-DEV-f9c6d1e34d94.json", "com.stelinno.uddi");
		else
			em = emf.createDefaultEntityManager("com.stelinno.uddi");
		
		return em;
	}
	
	@Bean
	String version() {
		System.out.println("called version()!");
		return "V1.0.2017-07-04-19:38";
	}
}
