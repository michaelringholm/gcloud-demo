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

package com.stelinno.uddi.search.obsolote;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.SearchServiceFactory;
import com.stelinno.uddi.search.IndexHelper;
// @formatter:off
// CHECKSTYLE:OFF
// [START schema_import]
import com.google.appengine.api.search.Field.FieldType;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.GetIndexesRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Schema;
// [END schema_import]
// @formatter:on
// CHECKSTYLE:ON

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchemaServlet {

	@Autowired
	private IndexHelper indexHelper;
	
	@Autowired
	private String SCHEMA_CONTROLLER_SEARCH_INDEX;
	
	@RequestMapping(value="/schema", method=RequestMethod.GET)
	public String schema() {
		Document doc = Document.newBuilder().setId("theOnlyCar")
				.addField(Field.newBuilder().setName("maker").setText("Toyota"))
				.addField(Field.newBuilder().setName("price").setNumber(300000))
				.addField(Field.newBuilder().setName("color").setText("lightblue"))
				.addField(Field.newBuilder().setName("model").setText("Prius")).build();
		try {
			indexHelper.addToIndex(SCHEMA_CONTROLLER_SEARCH_INDEX, doc);
		} catch (InterruptedException e) {
			// ignore
		}
		// [START list_schema]
		GetResponse<Index> response = SearchServiceFactory.getSearchService()
				.getIndexes(GetIndexesRequest.newBuilder().setSchemaFetched(true).build());

		StringWriter sw = new StringWriter();
		// List out elements of each Schema
		for (Index index : response) {
			Schema schema = index.getSchema();
			for (String fieldName : schema.getFieldNames()) {
				List<FieldType> typesForField = schema.getFieldTypes(fieldName);
				// Just printing out the field names and types
				for (FieldType type : typesForField) {
					sw.write((index.getName() + ":" + fieldName + ":" + type.name()));
				}
			}
		}
		return sw.toString();
	}
}
