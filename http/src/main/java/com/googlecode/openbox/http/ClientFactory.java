package com.googlecode.openbox.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class ClientFactory {
	
	public static final int MAX_TOTAL = 200;
	public static final String USER_AGENT = "Default Httpclient-4.3";

	public static HttpClientBuilder getHttpClientBuilder() {
		return getDefaultHttpClientBuilder(MAX_TOTAL, USER_AGENT);
	}

	public static HttpClientBuilder getDefaultHttpClientBuilder(int maxTotal,
			String userAgent) {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(maxTotal);
		return HttpClients.custom().setConnectionManager(cm)
				.setUserAgent(userAgent);
	}

	public static CloseableHttpClient  createHttpClient(HttpClientBuilder httpClientBuilder) {
		return httpClientBuilder.build();
	}

}
