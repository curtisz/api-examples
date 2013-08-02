package ScalrApiClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;


public class ScalrApiClient {

	private static final String SCALR_KEY_ID = "xxxxxxxxxxxxxxx";
	private static final String SCALR_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
	private static final String API_URL = "https://api.scalr.net/";
	private static final String API_VERSION = "2.3.0";
	private static final String API_AUTH_VERSION = "3";
	
	public void doRequest () 
	throws Exception {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("Action", "FarmsList");
		params.put("Version", API_VERSION);
		params.put("AuthVersion", API_AUTH_VERSION);
		params.put("KeyID", SCALR_KEY_ID);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		params.put("Timestamp", df.format(new Date()));
		
		// Sign request
		String canonicalString = params.get("Action") + ":" + params.get("KeyID") + ":" + params.get("Timestamp"); 
		String signature = makeSignature(canonicalString, SCALR_KEY);
		params.put("Signature", signature);
		
		// Build query string
		String queryString = "";
		for (String key : params.keySet()) {
			queryString += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8") + "&";
		}
		queryString = queryString.substring(0, queryString.length()-1);
		
		// Execute HTTP request
		URL url = new URL(API_URL + "?" + queryString);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String line = null;
		while (null != (line = reader.readLine())) {
			// Output result
			System.out.println(line);
		}
		reader.close();
	}
	
	private String makeSignature (String payload, String key) 
	throws Exception {
		// Based on AWS signature algorithm
		//
		// The following HMAC/SHA1 code for the signature is taken from the
		// AWS Platform's implementation of RFC2104 (amazon.webservices.common.Signature)
		//
		// Acquire an HMAC/SHA1 from the raw key bytes.
		System.out.println(payload);
		
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");

		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);
		
		byte [] signedBytes = null;
		try {
			signedBytes = mac.doFinal(payload.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			signedBytes = mac.doFinal(payload.getBytes());
		}
	
		return new BASE64Encoder().encode(signedBytes);
	}
	
	public static void main(String[] args) 
	throws Exception {
		ScalrApiClient apiClient = new ScalrApiClient();
		apiClient.doRequest();
	}
}
