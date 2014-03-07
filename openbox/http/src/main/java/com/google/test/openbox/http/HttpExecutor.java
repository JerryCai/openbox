package com.google.test.openbox.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HttpContext;

public interface HttpExecutor<T extends Response> {
	
	HttpContext getContext();

	void setContext(HttpContext context);

	void registerHttpRequestProxy(RequestProxy httpRequestProxy);
	
	ExecutorMonitorManager getExecutorMonitorManager();
	
	int execute() throws ClientProtocolException, IOException;

	T getResponse();
	

}