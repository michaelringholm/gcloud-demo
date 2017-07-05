package com.stelinno.uddi.search;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.StatusCode;

import com.stelinno.entities.Service;
import com.stelinno.mappers.ServiceMapper;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/index")
public class SearchIndexController {

	private static final Logger logger = Logger.getLogger(SearchIndexController.class.getName());
	@Autowired private IndexHelper indexHelper;	
	@Autowired private String SEARCH_INDEX_CONTROLLER_INDEX_NAME;

	/***
	 * curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":"5118084088070144","name":"Sports Results 97","domain":"Sports", "subDomain":"Statistics","endpoint":"http://sports-service.azure.com"}' https://search-dot-stelinno-dev.appspot.com/index/add.ctl
	 * curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d "{\"id\":\"5118084088070144\",\"name\":\"Sports Results 97\",\"domain\":\"Sports\", \"subDomain\":\"Statistics\",\"endpoint\":\"http://sports-service.azure.com\"}" https://search-dot-stelinno-dev.appspot.com/index/add.ctl
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody String add(@RequestBody Service service) {
		try {
			logger.info(String.format("Starting to index service with name %s...", service.getName()));
			indexHelper.addToIndex(SEARCH_INDEX_CONTROLLER_INDEX_NAME, ServiceMapper.toDocument(service));
			logger.info(String.format("Done indexing service with name %s!", service.getName()));
		} catch (InterruptedException e) {
			// ignore
		}

		return "Service indexed!";
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
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public @ResponseBody String about() {
		logger.info("/index/about called!");

		return "about called!";
	}	
}
