package com.stelinno.uddi.search;

import com.google.apphosting.api.ApiProxy;
import com.google.gson.Gson;
import com.stelinno.mappers.ServiceMapper;
import com.stelinno.uddi.entities.Service;
import com.stelinno.http.GenericResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/index")
public class SearchIndexController {

	private static final Logger logger = Logger.getLogger(SearchIndexController.class.getName());
	@Autowired private IndexHelper indexHelper;	
	@Autowired private String SEARCH_INDEX_CONTROLLER_INDEX_NAME;
	@Autowired private HttpHeaders jsonHttpHeaders;
	@Autowired private Gson gson;

	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":"5118084088070144","name":"Sports Results 97","domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' https://search-dot-stelinno-dev.appspot.com/index/add.ctl
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":"5081456606969856","name":"Sports Results 4","domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' https://search-dot-stelinno-dev.appspot.com/index/add.ctl
	 * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d "{\"id\":\"5118084088070144\",\"name\":\"Sports Results 97\",\"domain\":\"Sports\", \"subDomain\":\"Statistics\",\"endpoint\":\"http://sports-service.azure.com\"}" https://search-dot-stelinno-dev.appspot.com/index/add.ctl
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<String> add(@RequestBody Service service) {
		try {
			logger.info(String.format("Starting to index service with name %s...", service.getName()));
			indexHelper.addToIndex(SEARCH_INDEX_CONTROLLER_INDEX_NAME, ServiceMapper.toDocument(service));
			logger.info(String.format("Done indexing service with name %s!", service.getName()));
		} catch (InterruptedException e) {
			// ignore
		}

		GenericResponse response = new GenericResponse();
		response.message = "Service was added to search index!";
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}
	
	/***
	 * curl https://search-dot-stelinno-dev.appspot.com/index/delete.ctl?serviceId=5760820306771968
	 * curl http://localhost:8080/index/delete.ctl?serviceId=5760820306771968
	 * @param serviceId
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public ResponseEntity<String> delete(String serviceId) {
		try {
			indexHelper.removeFromIndex(SEARCH_INDEX_CONTROLLER_INDEX_NAME, serviceId);
		}
		catch (RuntimeException e) {
			logger.log(Level.SEVERE, "Failed to delete documents", e);
		}
		
		GenericResponse response = new GenericResponse();
		response.message = "Service removed from search index!";
		return new ResponseEntity<String>(gson.toJson(response), jsonHttpHeaders, HttpStatus.OK);
	}	
	
	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":"5118084088070144","name":"Sports Results 97","domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' https://search-dot-stelinno-dev.appspot.com/index/addVoid.ctl
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d "{\"id\":\"5118084088070144\",\"name\":\"Sports Results 97\",\"domain\":\"Sports\", \"subDomain\":\"Statistics\",\"endpoint\":\"http://sports-service.azure.com\"}" https://search-dot-stelinno-dev.appspot.com/index/addVoid.ctl
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d "{\"id\":\"5118084088070144\",\"name\":\"Sports Results 97\",\"domain\":\"Sports\", \"subDomain\":\"Statistics\",\"endpoint\":\"http://sports-service.azure.com\"}" http://localhost:8080/index/addVoid.ctl
	 * @param service
	 * @return
	 */
	@RequestMapping(value="/addVoid", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody String addVoid(@RequestBody Service service) {
		logger.info(String.format("Starting to index service with name %s...", "void"));
		//indexHelper.addToIndex(SEARCH_INDEX_CONTROLLER_INDEX_NAME, ServiceMapper.toDocument(service));
		logger.info(String.format("Done indexing service with name %s!", "void"));

		return "VoidService indexed!";
	}	
	
	/***
	 * curl https://search-dot-stelinno-dev.appspot.com/index/about.ctl
	 * curl http://localhost:8080/index/about.ctl
	 * @return
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public @ResponseBody String about() {
		logger.info("/index/about called!");

		return "about called!";
	}	
	
	@RequestMapping("/host")
	public @ResponseBody String host(HttpServletRequest request) {
		//String hostName = request.getURI().getHost();
		// Message body required though ignored
		return request.getRequestURL().toString();
	}
	
	@RequestMapping("/ghost")
	public @ResponseBody String ghost(HttpServletRequest request) {		
		return ApiProxy.getCurrentEnvironment().getAttributes().get("com.google.appengine.runtime.default_version_hostname").toString();
	}
	
	@RequestMapping("/gappid")
	public @ResponseBody String gappid(HttpServletRequest request) {		
		return ApiProxy.getCurrentEnvironment().getAppId();
	}	
}
