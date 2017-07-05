package com.stelinno.uddi.search;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
// [END delete_import]

// CHECKSTYLE:OFF
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteController {
	private static final Logger LOG = Logger.getLogger(DeleteController.class.getSimpleName());
	
	@Autowired
	private IndexHelper indexHelper;
	
	@Autowired
	private String DELETE_CONTROLLER_SEARCH_INDEX;

	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public @ResponseBody Document delete() {
		// Put one document to avoid an error
		Document document = Document.newBuilder().addField(Field.newBuilder().setName("MyName").setText("My Text")).build();
		try {
			indexHelper.addToIndex(DELETE_CONTROLLER_SEARCH_INDEX, document);
		} catch (InterruptedException e) {
			// ignore
		}
		// [START delete_documents]
		try {
			// looping because getRange by default returns up to 100 documents
			// at a time
			List<String> docIds = new ArrayList<>();
			while (true) {				
				// Return a set of doc_ids.
				GetRequest request = GetRequest.newBuilder().setReturningIdsOnly(true).build();
				GetResponse<Document> response = indexHelper.getIndex(DELETE_CONTROLLER_SEARCH_INDEX).getRange(request);
				if (response.getResults().isEmpty()) {
					break;
				}
				for (Document doc : response) {
					docIds.add(doc.getId());
				}
				indexHelper.getIndex(DELETE_CONTROLLER_SEARCH_INDEX).delete(docIds);				
			}
		}
		catch (RuntimeException e) {
			LOG.log(Level.SEVERE, "Failed to delete documents", e);
		}
		return document;
	}
}
