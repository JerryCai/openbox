package com.googlecode.openbox.foo.request;

import com.googlecode.openbox.foo.ClientVersion;
import com.googlecode.openbox.http.requests.PostRequest;

public abstract class AbstractFooPostRequest extends AbstractFooRequest {

	public AbstractFooPostRequest(String url, ClientVersion version) {
		super(url, version);
	}

	@Override
	public String getMethod() {
		return PostRequest.METHOD_NAME;
	}
}
