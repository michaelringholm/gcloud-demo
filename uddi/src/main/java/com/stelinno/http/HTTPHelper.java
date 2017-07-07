package com.stelinno.http;

public interface HTTPHelper {
	public abstract SimpleHTTPResponse postJson(String json, String targetUrl);
}
