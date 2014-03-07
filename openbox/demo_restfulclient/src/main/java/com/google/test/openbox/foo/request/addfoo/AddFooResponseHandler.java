package com.google.test.openbox.foo.request.addfoo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.google.test.openbox.http.AbstractResponseHandler;
import com.google.test.openbox.http.responses.JsonResponse;

public class AddFooResponseHandler extends
		AbstractResponseHandler<JsonResponse<AddFooResponse>> {

	private AddFooResponseHandler() {
	};

	public static AddFooResponseHandler newInstance() {
		return new AddFooResponseHandler();

	}

	@Override
	public JsonResponse<AddFooResponse> handleResponse(
			HttpResponse response) throws ClientProtocolException, IOException {
		return new JsonResponse<AddFooResponse>(response,
				getHttpContext(), getExecutorMonitorManager(),
				AddFooResponse.class);
	}
}
