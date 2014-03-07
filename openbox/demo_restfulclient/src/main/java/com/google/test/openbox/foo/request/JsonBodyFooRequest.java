package com.google.test.openbox.foo.request;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;

import com.google.test.openbox.foo.ClientVersion;
import com.google.test.openbox.http.GsonFactory;

public abstract class JsonBodyFooRequest<T> extends AbstractFooRequest {

	public JsonBodyFooRequest(String url, ClientVersion version) {
		super(url, version);
	}

	protected abstract T getJsonObjectBody();

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
