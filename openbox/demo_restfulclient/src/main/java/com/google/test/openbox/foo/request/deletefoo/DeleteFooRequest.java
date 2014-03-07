package com.google.test.openbox.foo.request.deletefoo;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;

import com.google.test.openbox.foo.ClientVersion;
import com.google.test.openbox.foo.request.AbstractFooRequest;
import com.google.test.openbox.foo.request.RequestUtil;

public class DeleteFooRequest extends AbstractFooRequest {
	public static final String API_PATH = "foo";

	private String id;

	public DeleteFooRequest(String url, ClientVersion version,
			String id) {
		super(url, version);
		this.id = id;

		setApiPath();
	}

	@Override
	public String getRestPath() {
		return API_PATH + "/" + id;
	}

	@Override
	public HttpEntity getEntity() {
		return RequestUtil.createEmptyJsonTypeEntity();
	}

	@Override
	public String getMethod() {
		return HttpDelete.METHOD_NAME;
	}

}
