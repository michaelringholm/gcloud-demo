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

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@SuppressWarnings("serial")
@WebServlet(name = "newSearch", description = "Search: Search for a document", urlPatterns = "/search/newsearch")
public class NewSearchServlet extends HttpServlet {

  private static final String SEARCH_INDEX = "PrimaryUDDISearchIndex";

  private Index getIndex() {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName(SEARCH_INDEX).build();
    Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
    return index;
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
   
    Service service = null; 
    try {
    	service = new Service(1001, "My Service 1", "Test", "Sub Test 1", "http://myservice1.stelinno.com/");
    	IndexHelper.addToIndex(SEARCH_INDEX, ServiceMapper.toDocument(service));
    	service = new Service(1007, "My Service 2", "Test", "Sub Test 2", "http://myservice2.stelinno.com/");
    	IndexHelper.addToIndex(SEARCH_INDEX, ServiceMapper.toDocument(service));
    	service = new Service(2005, "My Service 3", "Test", "Sub Test 3", "http://myservice3.stelinno.com/");
    	IndexHelper.addToIndex(SEARCH_INDEX, ServiceMapper.toDocument(service));
    	service = new Service(4987, "My Service 4", "Test", "Sub Test 4", "http://myservice4.stelinno.com/");
    	IndexHelper.addToIndex(SEARCH_INDEX, ServiceMapper.toDocument(service));
    } catch (InterruptedException e) {
      // ignore
    }
    // [START search_document]
    final int maxRetry = 3;
    int attempts = 0;
    int delay = 2;
    while (true) {
      try {
        String queryString = "product = piano AND price < 5000";
        Results<ScoredDocument> results = getIndex().search(queryString);

        // Iterate over the documents in the results
        for (ScoredDocument document : results) {
          // handle results
          out.print("maker: " + document.getOnlyField("maker").getText());
          out.println(", price: " + document.getOnlyField("price").getNumber());
        }
      } catch (SearchException e) {
        if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())
            && ++attempts < maxRetry) {
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
    // We don't test the search result below, but we're fine if it runs without errors.
    out.println("Search performed");
    Index index = getIndex();
    // [START simple_search_1]
    index.search("rose water");
    // [END simple_search_1]
    // [START simple_search_2]
    index.search("1776-07-04");
    // [END simple_search_2]
    // [START simple_search_3]
    // search for documents with pianos that cost less than $5000
    index.search("product = piano AND price < 5000");
    // [END simple_search_3]
  }
}
