package com.stelinno.http;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.apphosting.api.ApiProxy;
import com.google.gson.Gson;
import com.stelinno.http.HTTPHelper;
import com.stelinno.uddi.CustomResponse;

public class GoogleHTTPHelper implements HTTPHelper {
	private static final Logger logger = Logger.getLogger(GoogleHTTPHelper.class.getName());
	@Autowired private Gson gson;
	
	/***
	 * https://cloud.google.com/appengine/docs/standard/java/issue-requests
	 * @param service
	 * @param targetUrl
	 * @return
	 */
	public SimpleHTTPResponse postJson(String json, String targetUrl) {
		SimpleHTTPResponse simpleHTTPResponse = new SimpleHTTPResponse();
		
		try {
			URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
			URL url = new URL(targetUrl);
			HTTPRequest httpRequest = new HTTPRequest(url, HTTPMethod.POST);
			httpRequest.addHeader(new HTTPHeader("X-Appengine-Inbound-Appid", ApiProxy.getCurrentEnvironment().getAppId()));
			httpRequest.setPayload(json.getBytes());
			httpRequest.setHeader(new HTTPHeader("Content-Type", "application/json; charset=UTF-8"));
			HTTPResponse httpResp = urlFetch.fetch(httpRequest);
			String content = new String(httpResp.getContent());
			simpleHTTPResponse.statusCode = httpResp.getResponseCode();
			simpleHTTPResponse.payload = gson.fromJson(content, CustomResponse.class);
			simpleHTTPResponse.reason = "";
			
			logger.log(Level.INFO, String.format("The http response from the server was %s", content));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return simpleHTTPResponse;
	}

}
