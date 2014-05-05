package com.googlecode.openbox.http.requests;

import com.googlecode.openbox.http.Request;

public abstract class PostRequest extends Request {
	public final static String METHOD_NAME = "POST";

	public PostRequest(String url){
		super(url);
	}
	
	public PostRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}
}
