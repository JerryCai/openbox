package com.googlecode.openbox.http.responses;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;

import com.google.gson.Gson;
import com.googlecode.openbox.http.AbstractResponse;
import com.googlecode.openbox.http.ExecutorMonitorManager;
import com.googlecode.openbox.http.GsonFactory;

public class JsonResponse<T> extends AbstractResponse {

	private T jsonObject;

	public JsonResponse(HttpResponse httpResponse, HttpContext httpContext,
			ExecutorMonitorManager executorMonitorManager, Class<T> classT) {
		super(httpResponse, httpContext, executorMonitorManager);
		jsonObject = new Gson().fromJson(getContent(), classT);
	}

	public T getJsonObject() {
		return jsonObject;
	}

	@Override
	public ContentType[] getSupportedContentTypes() {

		return new ContentType[] { getUtf8ContentType(ContentType.APPLICATION_JSON) };
	}

	public String toString() {
		return GsonFactory.createGson().toJson(jsonObject);
	}

}
