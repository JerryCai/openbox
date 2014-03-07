package com.google.test.openbox.http.responses;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.google.test.openbox.http.AbstractResponseHandler;

public class CommonResponseHandler extends
		AbstractResponseHandler<CommonResponse> {

	@Override
	public CommonResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		return new CommonResponse(response, getHttpContext(),
				getExecutorMonitorManager());
	}

}
