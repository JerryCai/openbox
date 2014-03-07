package com.google.test.openbox.foo.request.error;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.google.test.openbox.http.AbstractResponseHandler;
import com.google.test.openbox.http.responses.JsonResponse;

public class ErrorResponseHandler extends
		AbstractResponseHandler<JsonResponse<ErrorResponse>> {

	private ErrorResponseHandler() {
	};

	public static ErrorResponseHandler newInstance() {
		return new ErrorResponseHandler();

	}

	@Override
	public JsonResponse<ErrorResponse> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new JsonResponse<ErrorResponse>(response, getHttpContext(),
				getExecutorMonitorManager(), ErrorResponse.class);
	}
}
