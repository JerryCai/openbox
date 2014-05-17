package com.googlecode.openbox.http;

import org.apache.http.client.ResponseHandler;
import org.apache.http.protocol.HttpContext;

public abstract class AbstractResponseHandler<T> implements ResponseHandler<T> {

	private HttpContext context;
	private ExecutorMonitorManager executorMonitorManager;


	public void setHttpContext(HttpContext context) {
		this.context = context;
	}

	public HttpContext getHttpContext() {
		return context;
	}

	public ExecutorMonitorManager getExecutorMonitorManager() {
		return executorMonitorManager;
	}

	public void setExecutorMonitorManager(
			ExecutorMonitorManager executorMonitorManager) {
		this.executorMonitorManager = executorMonitorManager;
	}
}
