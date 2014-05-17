package com.googlecode.openbox.http.requests;

import com.googlecode.openbox.http.Request;

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
