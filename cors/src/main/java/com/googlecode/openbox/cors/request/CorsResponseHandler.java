package com.googlecode.openbox.cors.request;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.googlecode.openbox.http.AbstractResponseHandler;

public class CorsResponseHandler extends AbstractResponseHandler<CorsResponse> {

	private CorsResponseHandler() {
	};

	public static CorsResponseHandler newInstance() {
		return new CorsResponseHandler();
	}

	@Override
	public CorsResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new CorsResponse(response, getHttpContext(),
				getExecutorMonitorManager());
	}
}
