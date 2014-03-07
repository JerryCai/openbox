package com.google.test.openbox.foo.request;

import com.google.test.openbox.foo.ClientVersion;
import com.google.test.openbox.http.requests.PostRequest;

public abstract class AbstractFooPostRequest extends AbstractFooRequest {

	public AbstractFooPostRequest(String url, ClientVersion version) {
		super(url, version);
	}

	@Override
	public String getMethod() {
		return PostRequest.METHOD_NAME;
	}
}
