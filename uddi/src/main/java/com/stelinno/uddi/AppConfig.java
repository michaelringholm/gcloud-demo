package com.stelinno.uddi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.apphosting.api.ApiProxy;
import com.google.gson.Gson;
import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;
import com.stelinno.http.GoogleHTTPHelper;
import com.stelinno.http.HTTPHelper;
import com.stelinno.http.StandardHTTPHelper;

@Configuration
@ComponentScan("com.stelinno.uddi")
public class AppConfig {
	
	private boolean onGCP = ApiProxy.getCurrentEnvironment() != null;
	
	@Bean 
	public EntityManager entityManager() {
		
		/*boolean local = new File("force-local.txt").exists();
		Logger.getGlobal().info(String.format("local = %b", local));
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}*/		
		
		EntityManagerFactory emf = EntityManagerFactory.getInstance();
		EntityManager em = null;
		
		//Logger.getGlobal().info(String.format("hostname = %s", hostName));
		
		if(onGCP)		
			em = emf.createDefaultEntityManager("com.stelinno.uddi");
		else
			em = emf.createEntityManager("stelinno-dev", "Stelinno-DEV-f9c6d1e34d94.json", "com.stelinno.uddi");
		
		return em;
	}

	
	@Bean String baseUDDISearchServiceUrl() {
		return "http://search-dot-stelinno-dev.appspot.com";
		//return "uddi-search2.stelinno.com";
		//return "http://uddi-search2.stelinno.com";
		//return "https://search-dot-stelinno-dev.appspot.com";
	}
	
	@Bean HttpHeaders jsonHttpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return httpHeaders;
	}
	
	@Bean String  UDDI_PRIMARY_SEARCH_INDEX() {
		return "UDDI_PRIMARY_SEARCH_INDEX";
	}
	
	@Bean Gson gson() {
		return new Gson();
	}
	
	@Bean HTTPHelper httpHelper() {
		if(onGCP)
			return new GoogleHTTPHelper();
		else
			return new StandardHTTPHelper();
	}
	
	@Bean String version() {
		System.out.println("called version()!");
		return "V1.0.2017-07-10-12:45";
	}
}
