package com.stelinno.entities;

public class Service {

	public Service() {
		
	}
	
    public Service(long id, String name, String domain, String subDomain, String endpoint) {
    	this.id = id;
    	this.name = name;
    	this.domain = domain;
    	this.subDomain = subDomain;
    	this.endpoint = endpoint;
	}
    
    private long id;
    private String name;
    private String description;
    private String domain;
    private String subDomain;
    private String endpoint;
    private String supportChat;
    private String supportEmail;
    private String serviceType;
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSupportChat() {
		return supportChat;
	}

	public void setSupportChat(String supportChat) {
		this.supportChat = supportChat;
	}

	public String getSupportEmail() {
		return supportEmail;
	}

	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}