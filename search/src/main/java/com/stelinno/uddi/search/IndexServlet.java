/**
 * Copyright 2016 Google Inc.
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

package com.stelinno.uddi.search;

// @formatter:off
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

// CHECKSTYLE:OFF
// [START get_document_import]
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.GetResponse;
// [END get_document_import]
// @formatter:on
// CHECKSTYLE:ON

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Code snippet for getting a document from Index.
 */
@RestController
public class IndexServlet {

	@Autowired
	private IndexHelper indexHelper;

	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index() {
		Document document = Document.newBuilder().setId("AZ125")
				.addField(Field.newBuilder().setName("myField").setText("myValue")).build();
		try {
			indexHelper.addToIndex(indexHelper.SEARCH_INDEX, document);
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
			return "interrupted";
		}
		System.out.println("Indexed a new document.");
		// [START get_document]
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexHelper.SEARCH_INDEX).build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

		// Fetch a single document by its doc_id
		Document doc = index.get("AZ125");

		// Fetch a range of documents by their doc_ids
		GetResponse<Document> docs = index.getRange(GetRequest.newBuilder().setStartId("AZ125").setLimit(100).build());
		// [END get_document]
		return "myField: " + docs.getResults().get(0).getOnlyField("myField").getText();
	}
}
