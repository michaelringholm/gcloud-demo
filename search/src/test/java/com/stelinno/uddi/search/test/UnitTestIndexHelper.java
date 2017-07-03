package com.stelinno.uddi.search.test;

import java.util.UUID;

import com.google.appengine.api.search.Document;
import com.stelinno.uddi.search.IndexHelper;

public class UnitTestIndexHelper extends IndexHelper {
		
	@Override
	public void addToIndex(String indexName, Document document) throws InterruptedException {		
		indexName = indexName + UUID.randomUUID().toString();
		super.addToIndex(indexName, document);
	}
}
