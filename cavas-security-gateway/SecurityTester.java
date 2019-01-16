package de.zap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.json.JSONObject;
import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

import de.cavas.EurekaController;
import de.cavas.InstanceSecurityTest;

//@RestController
public class SecurityTester {

	private final static String USER_AGENT = "Mozilla/5.0";
	private static final String ZAP_ADDRESS = "localhost";
	private static final int ZAP_PORT = 8181;
	private static final String ZAP_API_KEY = null;
	private static final String TESTING_MODE = "strict";
	private static EurekaClient discoveryClient;
	
	
	public static void main(String[] args)
			throws ClientProtocolException, IOException, JSONException, ClientApiException {
		
		
//		EurekaController EurekaController;
//		InstanceInfo instance = discoveryClient.getNextServerFromEureka("STORES", false);

//		for (InstanceInfo instanceInfo2 : InstanceInfo) {
//			System.out.println(instance);
//		}
//		Map<String, String> targetList = new HashMap<String, String>();
//
//		targetList.put("DeviceService", "http://localhost:9000");
//		targetList.put("UserService", "http://localhost:9001");
//
//		for (Map.Entry<String, String> entry : targetList.entrySet()) {
//			// buffer.append(entry.getKey()).append(separator).append(entry.getValue()).append("\n");
//			SecurityTester.gateway(entry.getKey(), entry.getValue());
//		}
		// SecurityTester.gateway ("DeviceService","http://localhost:9000");
		// //("https://cavas-test.herokuapp.com");

	}
	
	/*
	 * Method for testing CloudRAID (CSB) microservices which are coded in Vertx
	 * 
	 */
	
	public void vertxCSBTests() throws ClientApiException, IOException, JSONException {		
		
		
		Map<String, String> targetList = new HashMap<String, String>();

		targetList.put("DeviceService", "http://localhost:9000");
		targetList.put("UserService", "http://localhost:9001");

		for (Map.Entry<String, String> entry : targetList.entrySet()) {
//			 buffer.append(entry.getKey()).append(separator).append(entry.getValue()).append("\n");
			try {
				SecurityTester.gateway(entry.getKey(), entry.getValue());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 try {
			SecurityTester.gateway ("DeviceService","http://localhost:9000");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //("https://cavas-test.herokuapp.com");

	}

	// @RequestMapping("/security")
	public String tester() throws ClientApiException, IOException, JSONException {
		Map<String, String> targetList = new HashMap<String, String>();

		targetList.put("DeviceService", "http://localhost:9000");
		targetList.put("UserService", "http://localhost:9001");
		targetList.put("FileService", "http://localhost:9002");
		targetList.put("AAService", "http://localhost:9003");

		for (Map.Entry<String, String> entry : targetList.entrySet()) {
			// buffer.append(entry.getKey()).append(separator).append(entry.getValue()).append("\n");
			SecurityTester.gateway(entry.getKey(), entry.getValue());
		}

		return "tested OK!!";
	}
	//

	public static void gateway(String microserviceName, String target)
			throws ClientApiException, IOException, JSONException {

		URL mutURL = new URL(target);
		InetAddress address = InetAddress.getByName(mutURL.getHost());
		System.out.println("starting test for " + microserviceName + " with url " + target);
		// Properties zapProperties = new Properties();
		// InputStream input = new FileInputStream("security.properties");
		// zapProperties.load(input);
		// System.out.println(zapProperties.getProperty("zap.host") + ":" +
		// Integer.parseInt(zapProperties.getProperty("zap.port")));
		ClientApi clientApi = getZapServer(); // new
												// ClientApi(zapProperties.getProperty("zap.host"),Integer.parseInt(zapProperties.getProperty("zap.port")));
		// ZAP_ADDRESS, ZAP_PORT

		// ClientApi clientApi = new ClientApi(ZAP_ADDRESS, ZAP_PORT);// getZapServer();
		// //ZAP_ADDRESS, ZAP_PORT
		System.out.println("starting scan");
		// test
		// ClientApi api2 = new ClientApi(ZAP_ADDRESS, ZAP_PORT);

		String swaggerUrl = target + "/api/spec.json"; // "/v2/api-docs";

		System.out.println("requesting OpenAPI from swaggerUrl : " + swaggerUrl);
		Map<String, String> map = new HashMap<>();
		map.put("url", swaggerUrl);
		ApiResponse openApiResp = clientApi.callApi("openapi", "action", "importUrl", map); // importUrl
		System.out.println("exploring the api for using OpenAPI  @instance registry " + swaggerUrl);

		String scanResult = null;
		System.out.println("prepping to test target : " + target);

		try {
			// Start spidering the target
			System.out.println("Spider : " + target);
			ApiResponse resp = clientApi.spider.scan(target, "10", null, null, null);

			// ApiResponse resp = clientApi.spider.scan(ZAP_API_KEY, target, null, null,
			// null, null);
			String scanid;
			int progress;

			// The scan now returns a scan id to support concurrent scanning
			scanid = ((ApiResponseElement) resp).getValue();

			// Poll the status until it completes
			while (true) {
				Thread.sleep(1000);
				progress = Integer.parseInt(((ApiResponseElement) clientApi.spider.status(scanid)).getValue());
				System.out.println("Spider progress : " + progress + "%");
				if (progress >= 100) {
					break;
				}
			}
			System.out.println("Spider complete");

			// Give the passive scanner a chance to complete
			System.out.println("take 5 !");
			Thread.sleep(2000);

			System.out.println("Active scan : " + target);
			resp = clientApi.ascan.scan(target, null, null, "Default Policy", null, null);

			// The scan now returns a scan id to support concurrent scanning
			scanid = ((ApiResponseElement) resp).getValue();
			System.out.println("Scan Idd : => " + ((ApiResponseElement) resp).getValue());

			// Poll the status until it completes
			while (true) {

				progress = Integer.parseInt(((ApiResponseElement) clientApi.ascan.status(scanid)).getValue());
				System.out.println("Active scan progress : " + progress + "%");
				if (progress >= 100) {
					break;
				}
				Thread.sleep(300000);// 5 minutes 300000 1 minute 60000
				System.out.println("stopping active  scan after one minute ");
				clientApi.ascan.stop(scanid);
				break;

			}

		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		ApiResponse hh = clientApi.core.numberOfAlerts(target);
		System.out.println("the numberof alerts for target: " + target + " is " + hh);

		JSONObject mut = new JSONObject();
		mut.put("microserviceName", microserviceName);// http://localhost:8761/
		mut.put("microservicePort", mutURL.getPort());
		mut.put("microserviceIpAddress", address.getHostAddress());
		// TODO - this has be returned from the eureka server
		// mut.put("microserviceId", info.getId());
		mut.put("timeStamp", InstanceSecurityTest.getTime());

		// URL to check the alerts from OWASP ZAP server endpoint -->
		// http://localhost:8181/JSON/core/view/alerts/?baseurl
		List<Alert> alertList = clientApi.getAlerts(target, 0, 0);

		// zapClient.core.alerts(target, start, count);

		System.out.println("the number of alerts is : " + alertList.get(0).getAlert());
		System.out.println("the number of alerts is : " + hh);
		de.cavas.model.Alert cavasAlert = new de.cavas.model.Alert();
		for (Alert alert : alertList) {
			System.out.println(alert.getAlert());

			mut.put("risk", alert.getRisk());

			mut.put("alert", alert.getAlert());
			mut.put("attack", alert.getAttack());
			mut.put("confidence", alert.getConfidence());
			mut.put("confidence", alert.getConfidence());
			mut.put("messageId", alert.getMessageId());
			mut.put("url", alert.getUrl());
			// cavasAlert.setUrl((alert.getUrl().toString()));
			mut.put("param", alert.getParam());
			// cavasAlert.setParam(alert.getParam().toString());
			mut.put("solution", alert.getSolution());
			// cavasAlert.setSolution(alert.getSolution());
			mut.put("cweid", alert.getCweId());
			// cavasAlert.setCweid(String.valueOf(alert.getCweId()));
			mut.put("wascid", alert.getWascId());
			// cavasAlert.setWascid(String.valueOf(alert.getWascId()));
			mut.put("attack", alert.getAttack());
			// cavasAlert.setAttack(alert.getAttack());
			mut.put("description", alert.getDescription());
			// cavasAlert.setDescription(alert.getDescription());
			mut.put("evidence", alert.getEvidence());
			// cavasAlert.setEvidence(alert.getEvidence());
			mut.put("name", alert.getName());
			// cavasAlert.setName(alert.getName());
			mut.put("pluginid", alert.getPluginId());
			// cavasAlert.setPluginId(alert.getPluginId());
			mut.put("reference", alert.getReference());

			System.out.println("setting the instance status to UP i.e.ready to receive traffic !");
			// System.out.println("alertString : ---- " + mut.toString().toString());
			// info.setStatus(com.netflix.appinfo.InstanceInfo.InstanceStatus.UP);
			System.out.println("saving to db ....");
			System.out.println(cavasAlert);
			System.out.println("roger ....");

			// walk-around for the bug i.e. send the results to the database server endpoint
			String reportAggregator = "http://localhost:8081/alerts";
			DefaultHttpClient client = new DefaultHttpClient();

			// trigger the scan report retrieval
			HttpPost post = new HttpPost(reportAggregator);
			post.addHeader("User-Agent", USER_AGENT);

			StringEntity input = null;

			try {
				input = new StringEntity(mut.toString().toString());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("sending the result : " + input);
			input.setContentType(MediaType.APPLICATION_JSON);
			post.setEntity(input);

			System.out.println("Sending persistence request for : ");

			HttpResponse response = client.execute(post);
			// get the results
			System.out.println("\nSending 'POST' request to URL : " + reportAggregator);
			System.out.println("Post parameters : " + post.getEntity());
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
			//
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			//

		}
		// alertRepository.save(cavasAlert);

	}

	public static void callScanner(String target) throws ClientProtocolException, IOException, JSONException {
		String urlLocal = "http://localhost:8081/EventService02/scanner";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlLocal);

		// add header
		post.addHeader("User-Agent", USER_AGENT); // x-amz-sns-message-type
		System.out.println("\nSending 'POST' request to URL : " + urlLocal);

		JSONObject object = new JSONObject();
		object.put("target", target);
		String request = object.toString();
		//
		StringEntity input = null;

		try {
			input = new StringEntity(request);
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		input.setContentType(MediaType.APPLICATION_JSON);
		post.setEntity(input);

		HttpResponse response = client.execute(post);
		System.out.println(
				"\nSending security test request to the deployed security scanner request to URL : " + urlLocal);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		System.out.println(result.toString());
	}

	public static String getTime() {

		DateTime date = new DateTime();
		DateTimeZone cet = DateTimeZone.forID("CET");
		DateTime dateR = date.withZone(cet);

		System.out.println(date);

		return dateR.toString();

	}

	public void addInstance(String instanceName) {

		List<String> instances = new ArrayList<String>();
		if (!((instances.size()) == 0))
			for (String string : instances) {
				if (instanceName.equals(string)) {
					break;
					// return;
				} else {
					instances.add(instanceName);

				}
			}
	}

	public static ClientApi getZapServer() throws IOException {
		Properties zapProperties = new Properties();
		InputStream input = new FileInputStream("security.properties");
		zapProperties.load(input);
		System.out.println(zapProperties.getProperty("zap.host") + ":" + zapProperties.getProperty("zap.port"));
		ClientApi clientApi = new ClientApi(zapProperties.getProperty("zap.host"),
				Integer.parseInt(zapProperties.getProperty("zap.port")));
		// ZAP_ADDRESS, ZAP_PORT
		return clientApi;
	}

}
