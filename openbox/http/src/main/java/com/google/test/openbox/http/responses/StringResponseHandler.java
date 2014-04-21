package com.google.test.openbox.http.responses;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.google.test.openbox.http.AbstractResponseHandler;

public class StringResponseHandler extends
		AbstractResponseHandler<StringResponse> {

	private static final StringResponseHandler instance = new StringResponseHandler();

	private StringResponseHandler() {
	}

	public static final StringResponseHandler getInstance() {
		return instance;
	}

	@Override
	public StringResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new StringResponse(response, getHttpContext(),
				getExecutorMonitorManager());
	}

}
