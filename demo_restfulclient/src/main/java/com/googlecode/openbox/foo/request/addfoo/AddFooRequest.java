package com.googlecode.openbox.foo.request.addfoo;

import com.googlecode.openbox.foo.ClientVersion;
import com.googlecode.openbox.foo.request.JsonBodyFooRequest;
import com.googlecode.openbox.http.requests.PostRequest;

public class AddFooRequest extends
		JsonBodyFooRequest<AddFooParam> {
	public static final String API_PATH = "foo";

	private final AddFooParam requestBody;

	public AddFooRequest(String url, ClientVersion version,
			AddFooParam requestBody) {
		super(url, version);
		setApiPath();
		this.requestBody = requestBody;
	}

	@Override
	public String getMethod() {
		return PostRequest.METHOD_NAME;
	}

	@Override
	public AddFooParam getJsonObjectBody() {
		return requestBody;
	}

	@Override
	public String getRestPath() {
		return API_PATH;
	}

}
