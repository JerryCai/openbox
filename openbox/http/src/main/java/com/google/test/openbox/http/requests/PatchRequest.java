package com.google.test.openbox.http.requests;

import com.google.test.openbox.http.Request;

public abstract class PatchRequest extends Request {
	public final static String METHOD_NAME = "PATCH";

	public PatchRequest(String url) {
		super(url);
	}

	public PatchRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

}
