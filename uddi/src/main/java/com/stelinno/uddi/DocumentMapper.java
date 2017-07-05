package com.stelinno.uddi;

import com.google.appengine.api.search.Document;

public abstract class DocumentMapper {
	public static Service toService(Document document) {
		Service service = new Service();
	
		service.setId(Long.parseLong(document.getId()));
		service.setName(document.getOnlyField("name").getText());
		service.setDomain(document.getOnlyField("domain").getText());
		service.setSubDomain(document.getOnlyField("subDomain").getText());
		service.setEndpoint(document.getOnlyField("endpoint").getText());
		service.setDescription(document.getOnlyField("description").getText());
		service.setSupportChat(document.getOnlyField("supportChat").getText());
		service.setSupportEmail(document.getOnlyField("supportEmail").getText());
		service.setServiceType(document.getOnlyField("serviceType").getText());
		
		return service;
	}
}
