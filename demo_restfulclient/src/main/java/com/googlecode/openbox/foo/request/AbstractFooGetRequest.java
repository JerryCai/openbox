package com.googlecode.openbox.foo.request;

import org.apache.http.HttpEntity;

import com.googlecode.openbox.foo.ClientVersion;
import com.googlecode.openbox.http.requests.GetRequest;

public abstract class AbstractFooGetRequest extends AbstractFooRequest {

	public AbstractFooGetRequest(String url, ClientVersion version) {
		super(url, version);
	}

	@Override
	public HttpEntity getEntity() {
		return null;
	}

	@Override
	public String getMethod() {
		return GetRequest.METHOD_NAME;
	}
}
