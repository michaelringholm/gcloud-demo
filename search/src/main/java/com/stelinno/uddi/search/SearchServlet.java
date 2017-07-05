package com.stelinno.uddi.search;

// @formatter:off
// [START search_document_import]
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.StatusCode;
// [END search_document_import]
import com.stelinno.entities.Service;
import com.stelinno.mappers.ServiceMapper;
// CHECKSTYLE:OFF
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
// @formatter:on
// CHECKSTYLE:ON

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchServlet {

	@Autowired
	private IndexHelper indexHelper;
	
	@Autowired
	private String SEARCH_CONTROLLER_SEARCH_INDEX;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search() {
		Service service = null;
		try {
			service = new Service(1001, "My Service 1", "Test", "Sub Test 1", "http://myservice1.stelinno.com/");
			indexHelper.addToIndex(SEARCH_CONTROLLER_SEARCH_INDEX, ServiceMapper.toDocument(service));
			service = new Service(1007, "My Service 2", "Test", "Sub Test 2", "http://myservice2.stelinno.com/");
			indexHelper.addToIndex(SEARCH_CONTROLLER_SEARCH_INDEX, ServiceMapper.toDocument(service));
			service = new Service(2005, "My Service 3", "Test", "Sub Test 3", "http://myservice3.stelinno.com/");
			indexHelper.addToIndex(SEARCH_CONTROLLER_SEARCH_INDEX, ServiceMapper.toDocument(service));
			service = new Service(4987, "My Service 4", "Test", "Sub Test 4", "http://myservice4.stelinno.com/");
			indexHelper.addToIndex(SEARCH_CONTROLLER_SEARCH_INDEX, ServiceMapper.toDocument(service));
			service = new Service(4989, "Parcel Tracking Service", "Shipping", "Parcel",
					"http://parcel-tracking.stelinno.com/");
			indexHelper.addToIndex(SEARCH_CONTROLLER_SEARCH_INDEX, ServiceMapper.toDocument(service));
		} catch (InterruptedException e) {
			// ignore
		}
		// [START search_document]
		final int maxRetry = 3;
		int attempts = 0;
		int delay = 2;
		while (true) {
			try {
				String queryString = "name = parcel AND domain = shipping";
				Results<ScoredDocument> results = indexHelper.getIndex(SEARCH_CONTROLLER_SEARCH_INDEX).search(queryString);

				// Iterate over the documents in the results
				for (ScoredDocument document : results) {
					// handle results
					System.out.println("name: " + document.getOnlyField("name").getText());
					System.out.println(", domain: " + document.getOnlyField("domain").getText());
					System.out.println(", subDomain: " + document.getOnlyField("subDomain").getText());
				}
			} catch (SearchException e) {
				if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode()) && ++attempts < maxRetry) {
					// retry
					try {
						Thread.sleep(delay * 1000);
					} catch (InterruptedException e1) {
						// ignore
					}
					delay *= 2; // easy exponential backoff
					continue;
				} else {
					throw e;
				}
			}
			break;
		}
		// [END search_document]
		// We don't test the search result below, but we're fine if it runs
		// without errors.
		
		Index index = indexHelper.getIndex(SEARCH_CONTROLLER_SEARCH_INDEX);
		// [START simple_search_1]
		index.search("service");
		// [END simple_search_1]
		// [START simple_search_2]
		// index.search("1776-07-04");
		// [END simple_search_2]
		// [START simple_search_3]
		// search for documents with pianos that cost less than $5000
		// index.search("product = piano AND price < 5000");
		// [END simple_search_3]
		return "Search performed";
	}
}
