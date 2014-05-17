package com.googlecode.openbox.http.requests;

import org.apache.http.HttpEntity;

import com.googlecode.openbox.http.Request;

public class GetRequest extends Request {
	public final static String METHOD_NAME = "GET";

	
	public GetRequest(String url){
		super(url);
	}
	
	public GetRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

	@Override
	public HttpEntity getEntity() {
		return null;
	}

}
