package com.google.test.openbox.foo.request;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;

public final class RequestUtil {
	private RequestUtil() {
	}

	public static HttpEntity createEmptyJsonTypeEntity() {
		return EntityBuilder.create().setText("")
				.setContentType(ContentType.APPLICATION_JSON).build();
	}

}
