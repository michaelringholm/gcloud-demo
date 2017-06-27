/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stelinno.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;
import com.stelinno.service.entities.Service;

@SpringBootApplication(scanBasePackages="com.stelinno.uddi")
@RestController
public class ServiceRegistryApp {
  @RequestMapping("/home")
  public String home() {
    return "Service Registry Home!";
  }
  
  /***
   * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name":"Sports Results", "domain":"Sports", "subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}' http://localhost:8080/register
   * @param serviceDraft
   */
  	@RequestMapping(value="/register", method=RequestMethod.POST)
	public void register(@RequestBody Service service) {
		service = entityManager.insert(service);
		System.out.println(String.format("service with ID %d created successfully", service.getId()));
	}
  	
  	
  	@Autowired
  	EntityManager entityManager;
  	
    /***
     * curl http://localhost:8080/get?serviceName=Sports%20Results
     * @param serviceDraft
     */
   	@RequestMapping(value="/get", method=RequestMethod.GET)
  	public @ResponseBody List<Service> get(String serviceName) {
  		EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM service WHERE name = @name");
	  	request.setNamedBinding("name", serviceName);
	  	QueryResponse<Service> response = entityManager.executeEntityQueryRequest(Service.class, request);
	  	List<Service> services = response.getResults();
	  	return services;
  	}  	
  	
   	/***
   	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name":"Sports Results", "domain":"Sports", "subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}' http://localhost:8080/upsert
   	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":"5710239819104256","name":"Sports Results", "domain":"Sports", "subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}' http://localhost:8080/upsert
   	 * @param service
   	 */
  	@RequestMapping(value="/upsert", method=RequestMethod.POST)
	public void upsert(@RequestBody Service service) {
		service = entityManager.upsert(service);
		System.out.println(String.format("service with ID %d created successfully", service.getId()));
	}  	

  /**
   * <a href="https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
   * App Engine health checking</a> requires responding with 200 to {@code /_ah/health}.
   */
  @RequestMapping("/_ah/health")
  public String healthy() {
    // Message body required though ignored
    return "Still surviving.";
  }

  public static void main(String[] args) {
    SpringApplication.run(ServiceRegistryApp.class, args);
  }
}
