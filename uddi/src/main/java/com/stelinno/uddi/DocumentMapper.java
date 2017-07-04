package com.stelinno.uddi;

import com.google.appengine.api.search.Document;

public abstract class DocumentMapper {
	public static Service toService(Document document) {
		Service service = new Service();
	
		service.setId(Long.parseLong(document.getId()));
		service.setName(document.getOnlyField("name").getText());
		service.setName(document.getOnlyField("domain").getText());
		service.setName(document.getOnlyField("subDomain").getText());
		service.setName(document.getOnlyField("endpoint").getText());
		
		return service;
	}
}
