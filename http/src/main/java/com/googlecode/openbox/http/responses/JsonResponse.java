package com.googlecode.openbox.http.responses;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.http.AbstractResponse;
import com.googlecode.openbox.http.ExecutorMonitorManager;
import com.googlecode.openbox.http.GsonFactory;

public class JsonResponse<T> extends AbstractResponse {
	private static final Logger logger = LogManager.getLogger();

	private T jsonObject;

	public JsonResponse(HttpResponse httpResponse, HttpContext httpContext,
			ExecutorMonitorManager executorMonitorManager, Class<T> classT) {
		super(httpResponse, httpContext, executorMonitorManager);
		try {
			jsonObject = GsonFactory.createGson()
					.fromJson(getContent(), classT);
		} catch (Exception e) {
			//catch exception here , so that its basic response still can be used such as status code etc.
			logger.error(
					"jsonObject will be null , can't build JSON response object to type["
							+ classT.getName() + "] with response content ["
							+ getContent() + "]", e);
		}
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
