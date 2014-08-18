package com.googlecode.openbox.http.requests;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.googlecode.openbox.http.AbstractResponseHandler;
import com.googlecode.openbox.http.responses.XmlResponse;

public class XmlResponseHandler<T> extends
		AbstractResponseHandler<XmlResponse<T>> {
	private Class<T> classT;
	
	public XmlResponseHandler(Class<T> classT){
		this.classT = classT;
		
	}
	@Override
	public XmlResponse<T> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new XmlResponse<T>(response, getHttpContext(),
				getExecutorMonitorManager(),classT);
	}

}
