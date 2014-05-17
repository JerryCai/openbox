package com.googlecode.openbox.foo.request.getfoo;

import com.googlecode.openbox.foo.ClientVersion;
import com.googlecode.openbox.foo.request.AbstractFooGetRequest;

public class GetFooRequest extends AbstractFooGetRequest {
	public static final String API_PATH = "foo";

	public static final String HADER_NAME_PASSWORD = "password";

	private String fooid;

	public GetFooRequest(String url, ClientVersion version,
			String fooid) {
		super(url, version);
		this.fooid = fooid;
		
		setApiPath();
	}

	public void setFooPassword(String password) {
		setHeader(HADER_NAME_PASSWORD, password);
	}

	public void removeMeetingPassword() {
		removeHeader(HADER_NAME_PASSWORD);
	}

	@Override
	public String getRestPath() {
		return API_PATH + "/" + fooid;
	}

}
