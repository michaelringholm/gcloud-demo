package com.stelinno.uddi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.apphosting.api.ApiProxy;
import com.google.gson.Gson;
import com.jmethods.catatumbo.EntityManager;
import com.stelinno.uddi.entities.Service;

@RestController
@RequestMapping("/dns")
public class DNSController {

	private static final Logger logger = Logger.getLogger(DNSController.class.getName());
	@Autowired EntityManager entityManager;
	@Autowired private Gson gson;
	
	/***
	 * curl https://uddi-dot-stelinno-dev.appspot.com/dns/lookup.ctl?hostName=www.amazon.com
	 * @return
	 */
	@RequestMapping(value = "/lookup", method = RequestMethod.GET)
	public String lookup(String hostName) {		
		StringBuffer response = new StringBuffer();
		try {
			InetAddress[] inetAddrArr = SystemDefaultDnsResolver.INSTANCE.resolve(hostName);
			InetAddress inetAddr = inetAddrArr[0];
			response.append(String.format("{ \"canonical hostname\":\"%s\", ", inetAddr.getCanonicalHostName()));
			response.append(String.format("\"hostname\":\"%s\", ", inetAddr.getHostName()));
			response.append(String.format("\"ip\":\"%s\" }", inetAddr.toString()));
			logger.log(Level.INFO, String.format("InetAddr is %s!", response));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.append(e.getMessage());
		}
		
		return response.toString();
	}
	
	/***
	 * curl http://localhost:8080/dns/url.ctl?hostName=http://amazon.com
	 * curl http://uddi-dot-stelinno-dev.appspot.com/dns/url.ctl?hostName=http://search-dot-stelinno-dev.appspot.com
	 * @param hostName
	 * @return
	 */
	@RequestMapping(value = "/url", method = RequestMethod.GET)
	public String url(String hostName) {	
		StringBuffer response = new StringBuffer("{ ");
		try {
			URL url = new URL(hostName);
			response.append(String.format("\"host\":\"%s\", ", url.getHost()));
			url.openConnection().connect();
			response.append(String.format("\"connect\":\"%s\"", "OK"));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.append(String.format("\"error\":\"%s\"", e.getMessage()));
		}
		finally {
			response.append(" }");
		}
		
		return response.toString();
	}
	
	/***
	 * This method only works in the scope of GCP, will not work locally
	 * 
	 * doc: https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/urlfetch/URLFetchService
	 * curl http://localhost:8080/dns/url-fetch.ctl?hostName=http://amazon.com
	 * curl http://uddi-dot-stelinno-dev.appspot.com/dns/url-fetch.ctl?hostName=http://search-dot-stelinno-dev.appspot.com
	 * @param hostName
	 * @return
	 */
	@RequestMapping(value = "/url-fetch", method = RequestMethod.GET)
	public String urlFetch(String hostName) {	
		StringBuffer response = new StringBuffer("{ ");
		try {
			URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
			URL url = new URL(hostName);
			HTTPResponse httpResp = urlFetch.fetch(url);			
			response.append(String.format("\"response code\":\"%s\", ", httpResp.getResponseCode()));
			//url.openConnection().connect();
			//response.append(String.format("\"connect\":\"%s\"", "OK"));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.append(String.format("\"error\":\"%s\"", e.getMessage()));
		}
		finally {
			response.append(" }");
		}
		
		return response.toString();
	}
	
	/***
	 * This method only works in the scope of GCP, will not work locally
	 * 
	 * docs: https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/urlfetch/URLFetchService
	 * docs: https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/urlfetch/HTTPRequest
	 * 
	 * curl http://localhost:8080/dns/url-fetch-get.ctl?hostName=http://amazon.com
	 * http://uddi-dot-stelinno-dev.appspot.com/dns/url-fetch-get.ctl?hostName=https://search-dot-stelinno-dev.appspot.com
	 * http://uddi-dot-stelinno-dev.appspot.com/dns/url-fetch-get.ctl?hostName=https://search-dot-stelinno-dev.appspot.com/version.ctl
	 * @param hostName
	 * @return
	 */
	@RequestMapping(value = "/url-fetch-get", method = RequestMethod.GET)
	public String urlFetchGet(String hostName) {	
		StringBuffer response = new StringBuffer("{ ");
		try {
			URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
			URL url = new URL(hostName);
			HTTPRequest httpRequest = new HTTPRequest(url, HTTPMethod.GET);
			httpRequest.addHeader(new HTTPHeader("X-Appengine-Inbound-Appid", ApiProxy.getCurrentEnvironment().getAppId()));
			HTTPResponse httpResp = urlFetch.fetch(httpRequest);
			String content = new String(httpResp.getContent());
			response.append(String.format("\"response code\":\"%s\", ", httpResp.getResponseCode()));
			response.append(String.format("\"content\":\"%s\", ", content));
			//url.openConnection().connect();
			//response.append(String.format("\"connect\":\"%s\"", "OK"));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.append(String.format("\"error\":\"%s\"", e.getMessage()));
		}
		finally {
			response.append(" }");
		}
		
		return response.toString();
	}
	
	/***
	 * This method only works in the scope of GCP, will not work locally
	 * 
	 * docs: https://cloud.google.com/appengine/docs/standard/java/issue-requests
	 * docs: https://github.com/GoogleCloudPlatform/java-docs-samples/blob/master/appengine-java8/urlfetch/src/main/java/com/example/appengine/UrlFetchServlet.java
	 * docs: https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/urlfetch/URLFetchService
	 * docs: https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/urlfetch/HTTPRequest
	 * docs: http://www.programcreek.com/java-api-examples/index.php?api=com.google.appengine.api.urlfetch.HTTPMethod
	 * 
	 * hostname: http://uddi-dot-stelinno-dev.appspot.com/dns/url-fetch-get.ctl?hostName=https://search-dot-stelinno-dev.appspot.com/index/add.ctl
	 * @param hostName
	 * @return
	 */
	@RequestMapping(value = "/url-fetch-post", method = RequestMethod.GET)
	public String urlFetchPost(String hostName) {
		StringBuffer response = new StringBuffer("{ ");
		try {
			URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
			URL url = new URL(hostName);
			HTTPRequest httpRequest = new HTTPRequest(url, HTTPMethod.POST);
			httpRequest.addHeader(new HTTPHeader("X-Appengine-Inbound-Appid", ApiProxy.getCurrentEnvironment().getAppId()));
			Service service = new Service(0, "My Service", "A", "B", "C");
			service.setId(1);
			String json = gson.toJson(service);
			httpRequest.setPayload(json.getBytes());
			httpRequest.setHeader(new HTTPHeader("Content-Type", "application/json; charset=UTF-8"));
			HTTPResponse httpResp = urlFetch.fetch(httpRequest);
			String content = new String(httpResp.getContent());
			response.append(String.format("\"response code\":\"%s\", ", httpResp.getResponseCode()));
			response.append(String.format("\"content\":\"%s\", ", content));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			response.append(String.format("\"error\":\"%s\"", e.getMessage()));
		}
		finally {
			response.append(" }");
		}
		
		return response.toString();
	}	

	
	/***
	 * curl https://uddi-dot-stelinno-dev.appspot.com/dns/big-test.ctl
	 * @return
	 */
	@RequestMapping(value = "/big-test", method = RequestMethod.GET)
	public String bigTest() {		
		try {
			InetAddress[] inetAddr = SystemDefaultDnsResolver.INSTANCE.resolve("www.google.com");
			inetAddr = SystemDefaultDnsResolver.INSTANCE.resolve("www.amazon.com");
			inetAddr = SystemDefaultDnsResolver.INSTANCE.resolve("uddi-search2.stelinno.com");
			inetAddr = SystemDefaultDnsResolver.INSTANCE.resolve("search-dot-stelinno-dev.appspot.com");			
			//inetAddr = SystemDefaultDnsResolver.INSTANCE.resolve("https://search-dot-stelinno-dev.appspot.com");
			//inetAddr = SystemDefaultDnsResolver.INSTANCE.resolve("http://uddi-search2.stelinno.com");
			//inetAddr = SystemDefaultDnsResolver.INSTANCE.resolve(baseUDDISearchServiceUrl);
			logger.log(Level.INFO, String.format("InetAddr is %s!", inetAddr.toString()));
		    /*httpGet = new HttpGet("https://uddi-search2.stelinno.com");
		    response1 = httpclient.execute(httpGet);*/
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
		    //HttpGet httpGet = new HttpGet("http://uddi-search2.stelinno.com");
			HttpGet httpGet = new HttpGet("http://search-dot-stelinno-dev.appspot.com");
			
			// Add appid header if we are running in the google cloud, otherwise you can't call another app, locally it will fail though!
			if(ApiProxy.getCurrentEnvironment() != null)
				httpGet.addHeader("X-Appengine-Inbound-Appid", ApiProxy.getCurrentEnvironment().getAppId());
			
		    CloseableHttpResponse response1 = httpclient.execute(httpGet);
		    //response1.getEntity().getContent().
		    BufferedReader br = new BufferedReader(new InputStreamReader((response1.getEntity().getContent())));
			String output = br.readLine(); 		 
			logger.log(Level.INFO, "get succeeded!");
		    logger.log(Level.INFO, response1.toString());
		    logger.log(Level.INFO, output);		    
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		for(int i=0; i<10; i++) {
			try {
				CloseableHttpClient httpclient = HttpClients.createDefault();
			    //HttpGet httpGet = new HttpGet("http://uddi-search2.stelinno.com");
				HttpPost httpPost = new HttpPost("http://search-dot-stelinno-dev.appspot.com/index/add.ctl");
				
				// Add appid header if we are running in the google cloud, otherwise you can't call another app, locally it will fail though!
				if(ApiProxy.getCurrentEnvironment() != null)
					httpPost.addHeader("X-Appengine-Inbound-Appid", ApiProxy.getCurrentEnvironment().getAppId());
				String json = gson.toJson(new Service(0, "My Name", "A", "B", "C"));
				StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				httpPost.setEntity(requestEntity);
			    CloseableHttpResponse response1 = httpclient.execute(httpPost);		    
			    //response1.getEntity().getContent().
			    BufferedReader br = new BufferedReader(new InputStreamReader((response1.getEntity().getContent())));
				String output = br.readLine();
				httpPost.releaseConnection();
				logger.log(Level.INFO, "post succeeded!");
			    logger.log(Level.INFO, response1.toString());
			    logger.log(Level.INFO, output);		    
			}
			catch(Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}		
		}
		
		return "dns big test done!";
	}
	

	@Autowired
	private String version;
	
	@RequestMapping("/version")
	public @ResponseBody String version() {
		// Message body required though ignored
		return version;
	}
	
	@RequestMapping("/host")
	public @ResponseBody String host(HttpServletRequest request) {
		//String hostName = request.getURI().getHost();
		// Message body required though ignored
		return request.getRequestURL().toString();
	}
	
	@RequestMapping("/ghost")
	public @ResponseBody String ghost(HttpServletRequest request) {		
		return ApiProxy.getCurrentEnvironment().getAttributes().get("com.google.appengine.runtime.default_version_hostname").toString();
	}
	
	@RequestMapping("/gappid")
	public @ResponseBody String gappid(HttpServletRequest request) {		
		return ApiProxy.getCurrentEnvironment().getAppId();
	}
}
