package vn.com.irtech.irbot.business.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ApiInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		traceRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		traceResponse(response);
		return response;
	}
	
	private void traceRequest(HttpRequest request, byte[] body) throws IOException {
		logger.info("===========================================================================request begin");
		logger.info("URI         : {}", request.getURI());
		logger.info("Method      : {}", request.getMethod());
		logger.info("Headers     : {}", request.getHeaders() );
		logger.info("Request body: {}", new String(body, "UTF-8"));
		logger.info("=============================================================================request end");
	}

	private void traceResponse(ClientHttpResponse response) throws IOException {
	    logger.info("==========================================================================response begin");
	    logger.info("Status code  : {}", response.getStatusCode());
	    logger.info("Status text  : {}", response.getStatusText());
	    logger.info("Headers      : {}", response.getHeaders());
	    
	    String responeBodyJson = null;
	    if (response.getBody() != null) {
	    	StringBuilder inputStringBuilder = new StringBuilder();
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
		    String line = bufferedReader.readLine();
		    while (line != null) {
		        inputStringBuilder.append(line);
		        inputStringBuilder.append('\n');
		        line = bufferedReader.readLine();
		    }
		    responeBodyJson = inputStringBuilder.toString();
		    logger.info("Response body: {}", responeBodyJson);
	    }
	    logger.info("===========================================================================response end");
	    
	    if (response.getStatusCode() != HttpStatus.OK) {
	    	JsonObject responseData = null;
			try {
				responseData = new JsonParser().parse(responeBodyJson).getAsJsonObject();
			} catch (JsonSyntaxException e) {
			}
			
			String errMsg = "Error when call Web API!";
			if (responseData != null) {
				errMsg = responseData.get("message").toString();
			}
			
			throw new RuntimeException(String.format("[%s] - %s", response.getStatusCode(), errMsg));
	    }
	    
	}

}
