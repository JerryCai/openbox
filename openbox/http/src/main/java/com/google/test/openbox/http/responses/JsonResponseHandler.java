package com.google.test.openbox.http.responses;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.google.test.openbox.http.AbstractResponseHandler;

public class JsonResponseHandler<T> extends
		AbstractResponseHandler<JsonResponse<T>> {

	private Class<T> classT;

	public JsonResponseHandler(Class<T> classT) {
		this.classT = classT;

	}

	@Override
	public JsonResponse<T> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		try {
			return new JsonResponse<T>(response, getHttpContext(),
					getExecutorMonitorManager(), classT);
		} catch (Exception e) {
			return null;
		}
	}
}
