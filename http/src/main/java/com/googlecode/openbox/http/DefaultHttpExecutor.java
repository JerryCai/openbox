package com.googlecode.openbox.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.http.monitors.TimeLineMonitor;

public class DefaultHttpExecutor<T extends Response> implements HttpExecutor<T> {
	private static final Logger logger = LogManager.getLogger();

	private AbstractResponseHandler<? extends T> responseHandler;

	private CloseableHttpClient httpClient;
	private Request request;
	private T response;
	private HttpContext context;
	private ExecutorMonitorManager executorMonitorManager;

	public DefaultHttpExecutor(CloseableHttpClient httpClient, Request request,
			AbstractResponseHandler<? extends T> responseHandler) {
		this.httpClient = httpClient;
		this.request = request;
		this.responseHandler = responseHandler;
		this.context = new BasicHttpContext();
		this.responseHandler.setHttpContext(this.context);
		this.executorMonitorManager = ExecutorMonitorManagerImpl.newInstance();
		this.responseHandler
				.setExecutorMonitorManager(this.executorMonitorManager);
	}

	@Override
	public void registerHttpRequestProxy(RequestProxy requestProxy) {
		requestProxy.executeProxy(request);
	}

	@Override
	public ExecutorMonitorManager getExecutorMonitorManager() {
		return executorMonitorManager;
	}

	@Override
	public int execute() throws ClientProtocolException, IOException {

		HttpRequestBase httpRequest = request.toRequest();
		if (logger.isInfoEnabled()) {
			logger.info(RequestConverter.getHttpRequestLog(httpRequest,
					request.getEntity()));
		}
		registerDefaultMonitors();
		executorMonitorManager.startMonitors();
		try {
			response = httpClient
					.execute(httpRequest, responseHandler, context);
		} finally {
			executorMonitorManager.endMonitors();
		}

		response.getTimeLine().printLog();
		return response.getStatusCode();
	}

	private void registerDefaultMonitors() {
		executorMonitorManager.register(TimeLineMonitor.create());
	}

	@Override
	public T getResponse() {
		return response;
	}

	@Override
	public HttpContext getContext() {
		return context;
	}

	@Override
	public void setContext(HttpContext context) {
		this.context = context;
	}

}
