package com.stelinno.uddi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.google.gson.Gson;
import com.jmethods.catatumbo.DatastoreKey;
import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.KeyQueryRequest;
import com.jmethods.catatumbo.QueryResponse;
import com.stelinno.http.HTTPHelper;
import com.stelinno.http.SimpleHTTPResponse;
import com.stelinno.uddi.entities.Service;

@RestController
public class UDDIController {
	@RequestMapping("/home")
	public String home() {
		return "UDDI Home!";
	}

	private static final Logger logger = Logger.getLogger(UDDIController.class.getName());
	@Autowired EntityManager entityManager;
	@Autowired private Gson gson;
	@Autowired private HttpHeaders jsonHttpHeaders;
	@Autowired private HTTPHelper httpHelper;
	@Autowired private String baseUDDISearchServiceUrl;

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
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' http://localhost:8080/insert 
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name":"Sports Results","domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' http://localhost:8080/insert 
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name":"Sports Results 2", "domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' http://localhost:8080/insert
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name":"Sports Results","domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' https://uddi-dot-stelinno-dev.appspot.com/insert.ctl
	 * @param service
	 */
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<String> insert(@RequestBody Service service) {
		GenericResponse response = new GenericResponse(service);
		
		if (service == null || service.getName() == null) {
			response.message = "You must provide a name for your service!";
			return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (getByName(service.getName()) != null) {
			response.message = "A service with the given name already exist!";
			return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		service = entityManager.insert(service);
		updateIndex(service);		
		response.data = service;
		response.message = "Service registered!";
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}

	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":"5081456606969856","name":"Sports Results","domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' http://localhost:8080/update 
	 * @param service
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<String> update(@RequestBody Service service) {
		GenericResponse response = new GenericResponse(service);
		
		if (service == null || service.getId() == 0) {
			response.message = "The Id of the service is incorrect!";
			return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (service.getName() == null) {
			response.message = "You must provide a name for your service!";
			return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Service existingService = getByName(service.getName());
		if (existingService != null && existingService.getId() != service.getId()) {
			response.message = "A service with the given name already exist!";
			return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		service = entityManager.update(service);
		updateIndex(service);
		response.data = service;
		response.message = "Service updated!";
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}

	/***
	 * curl http://localhost:8080/delete?name=Sports%20Results%202
	 * 
	 * @param service
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ResponseEntity<String> delete(long serviceId) {
		GenericResponse response = new GenericResponse();
		if (serviceId != 0) {
			response.message = "The id of the service is incorrect!";
			return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Service service = getById(serviceId);
		if (service == null) {
			response.message = "The service was not found!";
			return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		entityManager.delete(service);
		removeFromIndex(service);
		response.data = service;
		response.message = "Service deleted!";
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}
	
	/***
	 * curl http://localhost:8080/rebuild-index.ctl
	 * curl https://uddi-dot-stelinno-dev.appspot.com/rebuild-index.ctl
	 * 
	 * @param service
	 */
	@RequestMapping(value = "/rebuild-index", method = RequestMethod.GET)
	public ResponseEntity<String> rebuildIndex() {
		GenericResponse response = new GenericResponse();	
		List<Service> services = getAll();
		for(Service service : services) {
			logger.log(Level.INFO, String.format("Posting to URL [%s] ...", baseUDDISearchServiceUrl + "/index/add.ctl"));
			SimpleHTTPResponse httpResponse = httpHelper.postJson(gson.toJson(service), baseUDDISearchServiceUrl + "/index/add.ctl");
			if(httpResponse == null)
				logger.log(Level.SEVERE, "HttpResponse was null!");
			
			if(httpResponse.statusCode != HttpStatus.OK.value())
				logger.log(Level.SEVERE, httpResponse.reason);
			else
				logger.log(Level.INFO, String.format("Service with id %d and name %s was re-indexed!", service.getId(), service.getName()));
		}

		response.message = "Service search index was rebuild!";
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}
	
	/***
	 * test: curl https://uddi-dot-stelinno-dev.appspot.com/search.ctl?serviceName=parcel
	 * @param serviceName
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<String> search(String serviceName) {
		GenericResponse response = new GenericResponse();
		List<Service> servicesFound = new ArrayList<>();
		Results<ScoredDocument> results = getIndex().search(serviceName);		
		for (ScoredDocument document : results) {
			Logger.getGlobal().info("name: " + document.getOnlyField("name").getText());
			Logger.getGlobal().info("domain: " + document.getOnlyField("domain").getText());
			servicesFound.add(DocumentMapper.toService(document));
		}

		response.data = servicesFound;
		response.message = String.format("Found %d services!", servicesFound.size());
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}	
	
	/***
	 * test: curl https://uddi-dot-stelinno-dev.appspot.com/searchById.ctl?serviceId=5712536552865792
	 * test: curl https://localhost:8080/searchById.ctl?serviceId=5712536552865792
	 * @param serviceName
	 * @return
	 */
	@RequestMapping(value = "/searchById", method = RequestMethod.GET)
	public ResponseEntity<String> searchById(long serviceId) {
		GenericResponse response = new GenericResponse();
		Service service = getById(serviceId);

		response.data = service;
		response.message = "Found service!";
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}		
	
	/***
	 * For query syntax please see
	 * https://cloud.google.com/datastore/docs/reference/gql_reference curl
	 * http://localhost:8080/find?serviceName=Sports%20Results
	 * 
	 * @param serviceName
	 * @return
	 */
	@RequestMapping(value = "/searchByName", method = RequestMethod.GET)
	public ResponseEntity<String> searchByName(String serviceName) {
		GenericResponse response = new GenericResponse();
		final List<Service> servicesFound = new ArrayList<>();

		Service service = new Service();
		service.setName(serviceName);
		SimpleHTTPResponse httpResponse = httpHelper.postJson(gson.toJson(service), baseUDDISearchServiceUrl + "/index/search.ctl");				
		Results<ScoredDocument> results = (Results<ScoredDocument>)httpResponse.payload;
		
		//Results<ScoredDocument> results = getIndex().search(serviceName);
		for (ScoredDocument document : results) {
			logger.info("name: " + document.getOnlyField("name").getText());
			logger.info("domain: " + document.getOnlyField("domain").getText());
			servicesFound.add(DocumentMapper.toService(document));
		}
		
		response.message = String.format("Found %d services!", servicesFound.size());
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}
	
	/*private SimpleHTTPResponse searchIndex(Service service) {
		SimpleHTTPResponse response = httpHelper.postJson(gson.toJson(service), baseUDDISearchServiceUrl + "/index/search.ctl");				
		return response;
	}*/
	
	private SimpleHTTPResponse updateIndex(Service service) {
		SimpleHTTPResponse response = httpHelper.postJson(gson.toJson(service), baseUDDISearchServiceUrl + "/index/add.ctl");				
		return response;
	}
	
	private SimpleHTTPResponse removeFromIndex(Service service) {
		SimpleHTTPResponse response = httpHelper.postJson(gson.toJson(service), baseUDDISearchServiceUrl + "/index/add.ctl");				
		return response;
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
	
	private Service getById(long id) {
		//Object serv = entityManager.loadById(Service.class, id);
		Service service = entityManager.load(Service.class, id);
		//EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM service WHERE id = @id");
		//KeyQueryRequest request2 = entityManager.createKeyQueryRequest("SELECT * FROM service WHERE id = @id");
		//request2.setNamedBinding("id", id);
		//QueryResponse<DatastoreKey> response2 = entityManager.executeKeyQueryRequest(request2);
		//request.setNamedBinding("id", id);
		//QueryResponse<Service> response = entityManager.executeEntityQueryRequest(Service.class, request);
		//List<Service> services = response.getResults();

		/*Service serviceFound = null;
		if (services.size() > 0)
			serviceFound = services.get(0);*/

		return service;
	}
	
	/**
	 * Dangerous mostly for testing...
	 * @return
	 */
	private List<Service> getAll() {
		EntityQueryRequest request = entityManager.createEntityQueryRequest("SELECT * FROM service");
		QueryResponse<Service> response = entityManager.executeEntityQueryRequest(Service.class, request);
		List<Service> services = response.getResults();

		return services;
	}
	

	@Autowired
	private String UDDI_PRIMARY_SEARCH_INDEX;

	private Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(UDDI_PRIMARY_SEARCH_INDEX).build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		return index;
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

	@Autowired
	private String version;
	
	@RequestMapping("/version")
	public @ResponseBody String version() {
		// Message body required though ignored
		return version;
	}
	
	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name":"Sports Results", "domain":"Sports","subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}' http://localhost:8080/register 
	 * @param serviceDraft
	 */
	@Deprecated
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public void register(@RequestBody Service service) {
		service = entityManager.insert(service);
		System.out.println(String.format("service with ID %d created successfully", service.getId()));
	}
	
	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"name":"Sports Results", "domain":"Sports", "subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}' http://localhost:8080/upsert 
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":"5710239819104256","name":"Sports Results", "domain":"Sports","subDomain":"Statistics", "endpoint":"http://sports-service.azure.com"}' http://localhost:8080/upsert
	 * @param service
	 */
	@RequestMapping(value = "/upsert", method = RequestMethod.POST)
	@Deprecated
	public void upsert(@RequestBody Service service) {
		service = entityManager.upsert(service);
		System.out.println(String.format("service with ID %d created successfully", service.getId()));
	}
}
