package com.googlecode.openbox.http;

import org.apache.http.Header;

public interface Response {

	int getStatusCode();

	Header[] getHeaders();

	String getHeaderValue( String headerName);
	
	String getContent();

	byte[] getBodyData();

	TimeLine getTimeLine();
}
