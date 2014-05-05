package com.googlecode.openbox.http.responses;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;

import com.googlecode.openbox.http.AbstractResponse;
import com.googlecode.openbox.http.ExecutorMonitorManager;

public class StringResponse extends AbstractResponse {

	public StringResponse(HttpResponse httpResponse, HttpContext httpContext,
			ExecutorMonitorManager executorMonitorManager) {
		super(httpResponse, httpContext, executorMonitorManager);
	}

	@Override
	public ContentType[] getSupportedContentTypes() {
		return null;
	}

}
