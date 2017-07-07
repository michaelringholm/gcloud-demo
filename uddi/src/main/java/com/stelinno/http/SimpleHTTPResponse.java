package com.stelinno.http;

public class SimpleHTTPResponse {
	
	public SimpleHTTPResponse(int statusCode, Object payload) {
		this.statusCode = statusCode;
		this.payload = payload;
	}
	
	public SimpleHTTPResponse() {
	}

	public int statusCode;
	public Object payload;
	public String reason;
}
