package com.googlecode.openbox.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;

import com.googlecode.openbox.http.GsonFactory;
import com.googlecode.openbox.http.Request;

public abstract class JsonBodyRequest<T> extends Request {

	public JsonBodyRequest(String url) {
		super(url);
	}

	public JsonBodyRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	public abstract T getJsonObjectBody();

	@Override
	public HttpEntity getEntity() {
		String text = null;
		T jsonObjectBody = getJsonObjectBody();
		if (jsonObjectBody instanceof String) {
			// we support set the json text directly too
			text = (String) jsonObjectBody;
		} else {
			text = GsonFactory.createGson().toJson(jsonObjectBody);
		}

		return EntityBuilder.create().setText(text)
				.setContentType(ContentType.APPLICATION_JSON).build();
	}
}
