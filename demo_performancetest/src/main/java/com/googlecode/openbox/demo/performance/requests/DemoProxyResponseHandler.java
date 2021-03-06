package com.googlecode.openbox.demo.performance.requests;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.googlecode.openbox.http.AbstractResponseHandler;
import com.googlecode.openbox.http.responses.JsonResponse;

public class DemoProxyResponseHandler extends
		AbstractResponseHandler<JsonResponse<DemoProxyResponse>> {

	private DemoProxyResponseHandler() {
	};

	public static DemoProxyResponseHandler newInstance() {
		return new DemoProxyResponseHandler();

	}

	@Override
	public JsonResponse<DemoProxyResponse> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new JsonResponse<DemoProxyResponse>(response, getHttpContext(),
				getExecutorMonitorManager(), DemoProxyResponse.class);
	}

}
