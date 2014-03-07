package com.google.test.openbox.foo.request.getfoo;

import com.google.test.openbox.foo.ClientVersion;
import com.google.test.openbox.foo.request.AbstractFooGetRequest;

public class GetFooRequest extends AbstractFooGetRequest {
	public static final String API_PATH = "meeting";

	public static final String HADER_NAME_PASSWORD = "password";

	private String meetingId;

	public GetFooRequest(String url, ClientVersion version,
			String meetingId) {
		super(url, version);
		this.meetingId = meetingId;
		
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
		return API_PATH + "/" + meetingId;
	}

}
