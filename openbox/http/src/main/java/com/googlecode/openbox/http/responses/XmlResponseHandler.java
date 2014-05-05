package com.googlecode.openbox.http.responses;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.googlecode.openbox.http.AbstractResponseHandler;

public class XmlResponseHandler extends AbstractResponseHandler<XmlResponse> {

	private static final XmlResponseHandler instance = new XmlResponseHandler();

	private XmlResponseHandler() {
	}

	public static final XmlResponseHandler getInstance() {
		return instance;
	}

	@Override
	public XmlResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new XmlResponse(response, getHttpContext(),
				getExecutorMonitorManager());
	}

}
