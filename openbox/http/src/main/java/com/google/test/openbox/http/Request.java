package com.google.test.openbox.http;

import org.apache.http.client.utils.URIBuilder;

public abstract class Request extends AbstractRequestBuilder {
	
	public Request(String url) {
		super(url);
	}

	public Request(String url, String path) {
		super(url, path);
	}

	public Request(String scheme, String host, String path) {
		super(scheme, host, path);

	}

	public Request(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}
	
	public Request(URIBuilder builder) {
		super(builder);
	}

	public void addParameter(String param, String value) {
		getURIBuilder().addParameter(param, value);
	}
	
	public void setParameter(String param , String value){
		getURIBuilder().setParameter(param, value);
	}
	
	public void removeQuery(){
		getURIBuilder().removeQuery();
	}
	
	

}
