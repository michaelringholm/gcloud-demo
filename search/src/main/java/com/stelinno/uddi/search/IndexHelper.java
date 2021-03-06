package com.stelinno.uddi.search;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;

public class IndexHelper {

	private static final Map<String, Index> indexMap = new HashMap<>();
	/**
	 * Put a given document into an index with the given indexName.
	 * @param indexName The name of the index.
	 * @param document A document to add.
	 * @throws InterruptedException When Thread.sleep is interrupted.
	 */
	public void addToIndex(String indexName, Document document) throws InterruptedException {
		Index index = getIndex(indexName);

		final int maxRetry = 3;
		int attempts = 0;
		int delay = 2;
		while (true) {
			try {
				index.put(document);
			} catch (PutException e) {
				if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode()) && ++attempts < maxRetry) { // retrying
					Thread.sleep(delay * 1000);
					delay *= 2; // easy exponential backoff
					continue;
				} else {
					throw e; // otherwise throw
				}
			}
			break;
		}
	}

	public void removeFromIndex(String indexName, String id) {
		Index index = getIndex(indexName);
		index.delete(id);
	}
	
	Index getIndex(String indexName) {
		/*IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		return index;*/
		Index index = null;
		if(indexMap.containsKey(indexName))
			index = indexMap.get(indexName);
		else {
			IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
			index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
			indexMap.put(indexName, index);
		}
		return index;
	}

}
