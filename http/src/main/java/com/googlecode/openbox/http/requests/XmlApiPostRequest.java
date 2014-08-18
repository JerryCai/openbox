package com.googlecode.openbox.http.requests;

import com.googlecode.openbox.http.requests.PostRequest;

public abstract class XmlApiPostRequest extends XmlApiRequest{

	public XmlApiPostRequest(String url) {
		super(url);
	}
	
	public XmlApiPostRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}
	
	@Override
	public String getMethod() {
		return PostRequest.METHOD_NAME;
	}

}
