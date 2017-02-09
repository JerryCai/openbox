package com.googlecode.openbox.http.responses;

import com.googlecode.openbox.http.AbstractResponseHandler;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class StringResponseHandler extends
		AbstractResponseHandler<StringResponse> {

	public StringResponseHandler() {
	}

	public static final StringResponseHandler newInstance() {
		return new StringResponseHandler();
	}

	@Override
	public StringResponse handleResponse(HttpResponse response)
			throws IOException {
		return new StringResponse(response, getHttpContext(),
				getExecutorMonitorManager());
	}

}
