package com.google.test.openbox.http;

import org.apache.http.Header;

public interface Response {

	int getStatusCode();

	Header[] getHeaders();
	
	String getContent();
	
	TimeLine getTimeLine();
}
