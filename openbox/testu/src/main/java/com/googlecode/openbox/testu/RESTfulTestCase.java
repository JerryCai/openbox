package com.googlecode.openbox.testu;

import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public abstract class RESTfulTestCase extends AbstractTestCase {

	private static HttpClientBuilder httpClientBuilder = HttpClientBuilder
			.create();

	private static volatile CloseableHttpClient HTTP_CLIENT = null;

	public RESTfulTestCase(String name) {
		super(name);
		initHttpClient();
	}

	public void setUp() {
		super.setUp();
	}

	public void tearDown() {
		super.tearDown();
		// releaseHttpClient();
	}

	@BeforeTest
	public void beforeTest() {
		super.beforeTest();
	}

	@AfterTest
	public void afterTest() {
		super.afterTest();
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

	@SuppressWarnings("unused")
	private void releaseHttpClient() {
		if (null != getHttpClient()) {
			try {
				getHttpClient().close();
			} catch (IOException e) {
				throw new RuntimeException("close http client error !", e);
			}
		}
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
