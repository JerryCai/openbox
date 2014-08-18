package com.googlecode.openbox.http.responses;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import com.googlecode.openbox.http.AbstractResponseHandler;

public class DocumentResponseHandler extends AbstractResponseHandler<DocumentResponse> {

	private static final DocumentResponseHandler instance = new DocumentResponseHandler();

	private DocumentResponseHandler() {
	}

	public static final DocumentResponseHandler getInstance() {
		return instance;
	}

	@Override
	public DocumentResponse handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		return new DocumentResponse(response, getHttpContext(),
				getExecutorMonitorManager());
	}

}
