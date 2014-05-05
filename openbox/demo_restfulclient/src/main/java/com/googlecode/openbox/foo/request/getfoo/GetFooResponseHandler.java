package com.googlecode.openbox.foo.request.getfoo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.googlecode.openbox.http.AbstractResponseHandler;
import com.googlecode.openbox.http.responses.JsonResponse;

public class GetFooResponseHandler extends
		AbstractResponseHandler<JsonResponse<GetFooResponse>> {

	private GetFooResponseHandler() {
	};

	public static GetFooResponseHandler newInstance() {
		return new GetFooResponseHandler();

	}

	@Override
	public JsonResponse<GetFooResponse> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new JsonResponse<GetFooResponse>(response, getHttpContext(),
				getExecutorMonitorManager(), GetFooResponse.class);
	}
}
