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

package com.stelinno.uddi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;

@RestController
public class UDDIController {
	@RequestMapping("/home")
	public String home() {
		return "UDDI Home!";
	}

	@Autowired
	EntityManager entityManager;

	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X
	 * POST -d '{"name":"Sports Results", "domain":"Sports",
	 * "subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}'
	 * http://localhost:8080/register
	 * 
	 * @param serviceDraft
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void register(@RequestBody Service service) {
		service = entityManager.insert(service);
		System.out.println(String.format("service with ID %d created successfully", service.getId()));
	}

	/***
	 * curl http://localhost:8080/get?serviceName=Sports%20Results
	 * 
	 * @param serviceDraft
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody List<Service> get(String serviceName) {
		EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM service WHERE name = @name");
		request.setNamedBinding("name", serviceName);
		QueryResponse<Service> response = entityManager.executeEntityQueryRequest(Service.class, request);
		List<Service> services = response.getResults();
		return services;
	}

	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X
	 * POST -d '{"name":"Sports Results", "domain":"Sports",
	 * "subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}'
	 * http://localhost:8080/upsert curl -H "Accept: application/json" -H
	 * "Content-type: application/json" -X POST -d
	 * '{"id":"5710239819104256","name":"Sports Results", "domain":"Sports",
	 * "subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}'
	 * http://localhost:8080/upsert
	 * 
	 * @param service
	 */
	@RequestMapping(value = "/upsert", method = RequestMethod.POST)
	public void upsert(@RequestBody Service service) {
		service = entityManager.upsert(service);
		System.out.println(String.format("service with ID %d created successfully", service.getId()));
	}

	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X
	 * POST -d '{"domain":"Sports", "subDomain":"Statistics",
	 * "endpoint":"http://sports-service.azure.com"}'
	 * http://localhost:8080/insert curl -H "Accept: application/json" -H
	 * "Content-type: application/json" -X POST -d '{"name":"Sports Results",
	 * "domain":"Sports", "subDomain":"Statistics",
	 * "endpoint":"http://sports-service.azure.com"}'
	 * http://localhost:8080/insert curl -H "Accept: application/json" -H
	 * "Content-type: application/json" -X POST -d '{"name":"Sports Results 2",
	 * "domain":"Sports", "subDomain":"Statistics",
	 * "endpoint":"http://sports-service.azure.com"}'
	 * http://localhost:8080/insert
	 * 
	 * @param service
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public @ResponseBody Object insert(@RequestBody Service service) {
		if (service == null || service.getName() == null)
			throw new RuntimeException("You must provide a name for your service!");

		if (getByName(service.getName()) != null)
			throw new RuntimeException("A service with the given name already exist!");

		service = entityManager.insert(service);
		String success = String.format("Service with ID %d created successfully", service.getId());
		System.out.println(success);
		final long serviceId = service.getId();
		// return new Object(){ public String status="success"; public String
		// message="Service registered!"; public long id=serviceId; };
		return new Object() {
			public int status = HttpStatus.OK.value();
			public String message = "Service registered!";
			public long id = serviceId;
		};
	}

	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X
	 * POST -d '{"id":"5081456606969856","name":"Sports Results",
	 * "domain":"Sports", "subDomain":"Statistics",
	 * "endpoint":"http://sports-service.azure.com"}'
	 * http://localhost:8080/update
	 * 
	 * @param service
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Object update(@RequestBody Service service) {
		if (service == null || service.getId() == 0)
			throw new RuntimeException("The Id of the service is incorrect!");

		if (service.getName() == null)
			throw new RuntimeException("You must provide a name for your service!");

		if (getByName(service.getName()) != null)
			throw new RuntimeException("A service with the given name already exist!");

		service = entityManager.update(service);

		String success = String.format("Service with ID %d updated successfully", service.getId());
		System.out.println(success);
		final long serviceId = service.getId();
		return new Object() {
			public int status = HttpStatus.OK.value();
			public String message = "Service updated!";
			public long id = serviceId;
		};
	}

	/***
	 * curl http://localhost:8080/delete?name=Sports%20Results%202
	 * 
	 * @param service
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public @ResponseBody Object delete(String name) {
		if (name == null || name.length() < 1)
			throw new RuntimeException("The name of the service is incorrect!");

		Service service = getByName(name);
		if (service == null)
			throw new RuntimeException("The service was not found!");

		entityManager.delete(service);

		String success = String.format("Service with ID %d deleted successfully", service.getId());
		System.out.println(success);
		return new Object() {
			public int status = HttpStatus.OK.value();
			public String message = "Service deleted!";
		};
	}

	private Service getByName(String serviceName) {
		EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM service WHERE name = @name");
		request.setNamedBinding("name", serviceName);
		QueryResponse<Service> response = entityManager.executeEntityQueryRequest(Service.class, request);
		List<Service> services = response.getResults();

		Service serviceFound = null;
		if (services.size() > 0)
			serviceFound = services.get(0);

		return serviceFound;
	}

	private static final String PRIMARY_SEARCH_INDEX = "PRIMARY_SEARCH_INDEX";

	private Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(PRIMARY_SEARCH_INDEX).build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		return index;
	}

	/***
	 * For query syntax please see
	 * https://cloud.google.com/datastore/docs/reference/gql_reference curl
	 * http://localhost:8080/findByName?name=Sports%20Results
	 * 
	 * @param serviceName
	 * @return
	 */
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	private @ResponseBody Object find(String serviceName) {
		final List<Service> servicesFound = new ArrayList<>();
		/*
		 * EntityQueryRequest request = entityManager.
		 * createEntityQueryRequest("SELECT * FROM service WHERE name = @name");
		 * request.setNamedBinding("name", serviceName); QueryResponse<Service>
		 * response = entityManager.executeEntityQueryRequest(Service.class,
		 * request); servicesFound = response.getResults();
		 */

		/*
		 * String queryString = "product = piano AND price < 5000";
		 * Results<ScoredDocument> results = getIndex().search(queryString);
		 * 
		 * // Iterate over the documents in the results for (ScoredDocument
		 * document : results) { // handle results System.out.println("name: " +
		 * document.getOnlyField("name").getText());
		 * System.out.println("domain: " +
		 * document.getOnlyField("domain").getText()); //out.println(", price: "
		 * + document.getOnlyField("price").getNumber()); }
		 */
		Results<ScoredDocument> results = getIndex().search(serviceName);		
		for (ScoredDocument document : results) {
			System.out.println("name: " + document.getOnlyField("name").getText());
			System.out.println("domain: " + document.getOnlyField("domain").getText());
			servicesFound.add(DocumentMapper.toService(document));
		}

		return new Object() {
			public int status = HttpStatus.OK.value();
			public List<Service> services = servicesFound;
		};
		
		//return results;
	}

	/**
	 * <a href=
	 * "https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
	 * App Engine health checking</a> requires responding with 200 to
	 * {@code /_ah/health}.
	 */
	@RequestMapping("/_ah/health")
	public String healthy() {
		// Message body required though ignored
		return "Still surviving.";
	}

	@RequestMapping("/version")
	public @ResponseBody String version() {
		// Message body required though ignored
		return "V1.0.2017-07-04-12:01";
	}
}
