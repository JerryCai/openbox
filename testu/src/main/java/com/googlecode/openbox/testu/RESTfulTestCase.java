package com.googlecode.openbox.testu;

import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public abstract class RESTfulTestCase extends AbstractTestCase {

	private static HttpClientBuilder httpClientBuilder = HttpClientBuilder
			.create();

	private static volatile CloseableHttpClient HTTP_CLIENT = null;

	public RESTfulTestCase(String name) {
		super(name);
		initHttpClient();
	}

	private void initHttpClient() {
		if (null != HTTP_CLIENT) {
			return;
		}
		synchronized (RESTfulTestCase.class) {
			if (null != HTTP_CLIENT) {
				return;
			}

			if (null != getHttpClientConfig()) {
				httpClientBuilder = getHttpClientConfig();
			}
			if (null != getUserAgent()) {
				httpClientBuilder.setUserAgent(getUserAgent());
			}
			HTTP_CLIENT = httpClientBuilder.build();
		}
	}

	public void closeHttpClient() {
		HttpClientUtils.closeQuietly(getHttpClient());
	}

	/**
	 * you can custom your http client agent to your test client This may useful
	 * to tracking issue on share environment for trouble shooting .
	 * 
	 * @return
	 */
	public abstract String getUserAgent();

	/**
	 * We can custom our http client by HttpClientBuilder.create().setXXX ... to
	 * customer our http client , if return null , means you are use default
	 * httpClient .
	 * 
	 * @return
	 */
	public abstract HttpClientBuilder getHttpClientConfig();

	public static final CloseableHttpClient getHttpClient() {
		return HTTP_CLIENT;
	}
}
