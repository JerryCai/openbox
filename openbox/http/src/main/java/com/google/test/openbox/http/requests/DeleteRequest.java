package com.google.test.openbox.http.requests;

import com.google.test.openbox.http.Request;

public abstract class DeleteRequest extends Request {
	 public final static String METHOD_NAME = "DELETE";
	
	public DeleteRequest(String url) {
		super(url);
	}

	public DeleteRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}
}
