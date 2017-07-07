package com.stelinno.uddi.json;

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

import com.google.apphosting.api.ApiProxy;
import com.google.gson.Gson;
import com.stelinno.uddi.CustomResponse;
import com.stelinno.uddi.Service;
import com.stelinno.uddi.UDDIController;

public class JsonHelper {
	private static final Logger logger = Logger.getLogger(JsonHelper.class.getName());
	@Autowired private Gson gson;
	
	/***
	 * https://cloud.google.com/appengine/docs/standard/java/issue-requests
	 * @param service
	 * @param targetUrl
	 * @return
	 */
	public HttpResponse postJson(Service service, String targetUrl) {
		HttpResponse rawResponse = null;
		HttpClient httpClient = null;
		try {
			//httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
			httpClient = HttpClientBuilder.create().build();
			String json = gson.toJson(service);

			StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
			HttpPost postMethod = new HttpPost(targetUrl);
			// Add appid header if we are running in the google cloud, otherwise you can't call another app, locally it will fail though!
			if(ApiProxy.getCurrentEnvironment() != null)
				postMethod.addHeader("X-Appengine-Inbound-Appid", ApiProxy.getCurrentEnvironment().getAppId());
			
			postMethod.setEntity(requestEntity);
			rawResponse = httpClient.execute(postMethod);
			logger.log(Level.INFO, String.format("The raw response from the server was %s", rawResponse.toString()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return rawResponse;
	}

	public CustomResponse getJsonResponse(HttpResponse httpResponse) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
			String output;
			while ((output = br.readLine()) != null) {
				CustomResponse json = gson.fromJson(output, CustomResponse.class);
				return json;
			}
			throw new RuntimeException("Empty response from server!");
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new RuntimeException("Unable to get response from server!");
		}
	}
}
