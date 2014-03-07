package com.google.test.openbox.http.responses;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;

import com.google.test.openbox.http.AbstractResponse;
import com.google.test.openbox.http.ExecutorMonitorManager;

public class CommonResponse extends AbstractResponse {

	public CommonResponse(HttpResponse httpResponse, HttpContext httpContext,
			ExecutorMonitorManager executorMonitorManager) {
		super(httpResponse, httpContext, executorMonitorManager);
	}

	@Override
	public ContentType[] getSupportedContentTypes() {
		return null;
	}

}
