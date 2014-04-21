package com.google.test.openbox.http;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;

public interface RequestBuilder {
	
	URIBuilder getURIBuilder();

	RequestConfig getRequestConfig();
	
	List<NameValuePair> getHeaders();
	
	HttpEntity getEntity();

	String getMethod();
	
	HttpRequestBase toRequest();
}
