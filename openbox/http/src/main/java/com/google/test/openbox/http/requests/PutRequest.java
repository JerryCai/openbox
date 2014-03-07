package com.google.test.openbox.http.requests;

import com.google.test.openbox.http.Request;

public abstract class PutRequest extends Request {
	
	public PutRequest(String url) {
		super(url);
	}
	
	public PutRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	public final static String METHOD_NAME = "PUT";

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}
}
