package com.googlecode.openbox.foo.request.deletefoo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.googlecode.openbox.http.AbstractResponseHandler;
import com.googlecode.openbox.http.responses.JsonResponse;

public class DeleteFooResponseHandler extends
		AbstractResponseHandler<JsonResponse<DeleteFooResponse>> {

	private DeleteFooResponseHandler() {
	};

	public static DeleteFooResponseHandler newInstance() {
		return new DeleteFooResponseHandler();

	}

	@Override
	public JsonResponse<DeleteFooResponse> handleResponse(
			HttpResponse response) throws ClientProtocolException, IOException {
		return new JsonResponse<DeleteFooResponse>(response,
				getHttpContext(), getExecutorMonitorManager(),
				DeleteFooResponse.class);
	}
}
