package com.googlecode.openbox.http.extention;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;

import com.googlecode.openbox.http.ExecutorMonitor;
import com.googlecode.openbox.http.HttpExecutor;
import com.googlecode.openbox.http.RequestProxy;

public abstract class ExtendedAppClient implements AppClientExtendable {
	private CloseableHttpClient httpClient;
	private List<RequestProxy> mcProxys;
	private List<ExecutorMonitor> monitors;

	public ExtendedAppClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
		this.mcProxys = new LinkedList<RequestProxy>();
		this.monitors = new LinkedList<ExecutorMonitor>();
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	@Override
	public void registerProxy(RequestProxy proxy) {
		mcProxys.add(proxy);
	}

	@Override
	public void unregisterProxy(RequestProxy proxy) {
		mcProxys.remove(proxy);

	}

	@Override
	public void registerMonitor(ExecutorMonitor monitor) {
		monitors.add(monitor);
	}

	@Override
	public void unregisterMonitor(ExecutorMonitor monitor) {
		monitors.remove(monitor);
	}

	@Override
	public void cleanAllProxy() {
		mcProxys.clear();
	}

	@Override
	public void cleanAllMonitor() {
		monitors.clear();

	}

	public void registerAllPlugins(final HttpExecutor<?> httpExecutor) {
		for (RequestProxy mcProxy : mcProxys) {
			httpExecutor.registerHttpRequestProxy(mcProxy);
		}
		httpExecutor.getExecutorMonitorManager().register(monitors);
	}

}
