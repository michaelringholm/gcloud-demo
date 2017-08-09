package com.stelinno.uddi;

import com.google.appengine.api.search.Document;
import com.stelinno.uddi.entities.Service;

public abstract class DocumentMapper {
	public static Service toService(Document document) {
		Service service = new Service();
	
		service.setId(Long.parseLong(document.getId()));
		service.setName(document.getOnlyField("name").getText());
		if(document.getFieldCount("domain") > 0)
			service.setDomain(document.getOnlyField("domain").getText());
		if(document.getFieldCount("subDomain") > 0)
			service.setSubDomain(document.getOnlyField("subDomain").getText());
		if(document.getFieldCount("endpoint") > 0)
			service.setEndpoint(document.getOnlyField("endpoint").getText());
		if(document.getFieldCount("description") > 0)
			service.setDescription(document.getOnlyField("description").getText());
		if(document.getFieldCount("supportChat") > 0)
			service.setSupportChat(document.getOnlyField("supportChat").getText());
		if(document.getFieldCount("supportEmail") > 0)
			service.setSupportEmail(document.getOnlyField("supportEmail").getText());
		if(document.getFieldCount("serviceType") > 0)
			service.setServiceType(document.getOnlyField("serviceType").getText());
		
		return service;
	}
}
