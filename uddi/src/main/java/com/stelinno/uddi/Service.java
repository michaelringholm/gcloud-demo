package com.stelinno.uddi;

import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.Property;

@Entity(kind = "service")
public class Service {

	public Service() {
		
	}
	
    public Service(String name, String domain, String subDomain, String endpoint) {
    	this.name = name;
    	this.domain = domain;
    	this.subDomain = subDomain;
    	this.endpoint = endpoint;
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

	@Identifier
    private long id;

    @Property(name = "name")
    private String name;

    @Property(name = "domain")
    private String domain;

    @Property(name = "subDomain")
    private String subDomain;
    
    @Property(name = "endpoint")
    private String endpoint;
}