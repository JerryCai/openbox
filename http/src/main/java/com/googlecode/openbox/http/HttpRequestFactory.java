package com.googlecode.openbox.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;

public final class HttpRequestFactory {

	private HttpRequestFactory() {
	}

	public static HttpEntityEnclosingRequestBase createHasBodyRequest(final String method) {
		return new HttpEntityEnclosingRequestBase() {

			@Override
			public String getMethod() {
				return method;
			}

		};
	}

	public static HttpRequestBase createNoBodyRequestBase(final String method) {
		return new HttpRequestBase() {

			@Override
			public String getMethod() {
				return method;
			}

		};
	}
}
