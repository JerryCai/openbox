package com.google.test.openbox.http.responses;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.google.test.openbox.http.AbstractResponseHandler;

public class XmlResponseHandler extends AbstractResponseHandler<XmlResponse> {

	@Override
	public XmlResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new XmlResponse(response, getHttpContext(),
				getExecutorMonitorManager());
	}

}
