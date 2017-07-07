package com.stelinno.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.stelinno.http.HTTPHelper;
import com.stelinno.uddi.CustomResponse;

public class StandardHTTPHelper implements HTTPHelper {
	private static final Logger logger = Logger.getLogger(StandardHTTPHelper.class.getName());
	@Autowired private Gson gson;
	
	/***
	 * https://cloud.google.com/appengine/docs/standard/java/issue-requests
	 * @param service
	 * @param targetUrl
	 * @return
	 */
	public SimpleHTTPResponse postJson(String json, String targetUrl) {
		SimpleHTTPResponse simpleHTTPResponse = new SimpleHTTPResponse();
		HttpClient httpClient = null;
		try {
			//httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
			httpClient = HttpClientBuilder.create().build();
			StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
			HttpPost postMethod = new HttpPost(targetUrl);
			// Add appid header if we are running in the google cloud, otherwise you can't call another app, locally it will fail though!
			/*if(ApiProxy.getCurrentEnvironment() != null)
				postMethod.addHeader("X-Appengine-Inbound-Appid", ApiProxy.getCurrentEnvironment().getAppId());*/
			
			postMethod.setEntity(requestEntity);
			HttpResponse httpResponse = httpClient.execute(postMethod);
			simpleHTTPResponse.statusCode = httpResponse.getStatusLine().getStatusCode();
			simpleHTTPResponse.payload = toCustomReponse(httpResponse);
			simpleHTTPResponse.reason = httpResponse.getStatusLine().getReasonPhrase();
			logger.log(Level.INFO, String.format("The http response from the server was %s", httpResponse.toString()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return simpleHTTPResponse;
	}
	
	private CustomResponse toCustomReponse(HttpResponse httpResponse) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
			String output;
			while ((output = br.readLine()) != null) {
				CustomResponse response = gson.fromJson(output, CustomResponse.class);				
				return response;
			}
			throw new RuntimeException("Empty response from server!");
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException("Unable to get response from server!");
		}
	}
}
