package com.google.test.openbox.cors;

import java.io.IOException;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.test.openbox.cors.request.CorsRequest;
import com.google.test.openbox.cors.request.CorsResponse;
import com.google.test.openbox.cors.request.CorsResponseHandler;
import com.google.test.openbox.http.DefaultHttpExecutor;
import com.google.test.openbox.http.HttpClientException;
import com.google.test.openbox.http.HttpExecutor;
import com.google.test.openbox.http.Request;

public class PreflightCORSClientProxy extends CORSClientProxy {

	private String accessControlRequestHeaders;

	private Set<String> allowedMethods;

	public PreflightCORSClientProxy(String origin,
			String accessControlRequestHeaders) {
		super(origin);
		this.accessControlRequestHeaders = accessControlRequestHeaders;
	}

	public static PreflightCORSClientProxy newInstance(String originValue,
			String accessControlRequestHeaders) {
		return new PreflightCORSClientProxy(originValue,
				accessControlRequestHeaders);
	}

	@Override
	public void executeProxy(Request request) {
		super.executeProxy(request);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = null;
		try {
			CorsRequest corsRequest = new CorsRequest(request.getURIBuilder()
					.build().toString(), getOrigin(), request.getMethod(),
					accessControlRequestHeaders);
			HttpExecutor<CorsResponse> executor = new DefaultHttpExecutor<CorsResponse>(
					httpClient, corsRequest, CorsResponseHandler.newInstance());
			executor.execute();
			
			allowedMethods = executor.getResponse().getAllowedMethods();

		} catch (Exception e) {
			String message = "CORS options error as ";
			throw HttpClientException.create(message, e);
		} finally {
			HttpClientUtils.closeQuietly(httpResponse);
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Set<String> getAllowedMethods() {
		return allowedMethods;
	}

}
