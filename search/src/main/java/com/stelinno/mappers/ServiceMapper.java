package com.stelinno.mappers;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.stelinno.entities.Service;

public abstract class ServiceMapper {
	public static Document toDocument(Service service) {
		Document doc = Document.newBuilder()
				.setId(Long.toString(service.getId()))
		        .addField(Field.newBuilder().setName("name").setText(service.getName()))
		        .addField(Field.newBuilder().setName("domain").setText(service.getDomain()))
		        .addField(Field.newBuilder().setName("subDomain").setText(service.getSubDomain()))
		        .addField(Field.newBuilder().setName("endpoint").setText(service.getEndpoint()))
		        .addField(Field.newBuilder().setName("description").setText(service.getDescription()))
		        .addField(Field.newBuilder().setName("supportChat").setText(service.getSupportChat()))
		        .addField(Field.newBuilder().setName("supportEmail").setText(service.getSupportEmail()))
		        .addField(Field.newBuilder().setName("serviceType").setText(service.getServiceType()))
		        .build();
		
		return doc;
	}
}
