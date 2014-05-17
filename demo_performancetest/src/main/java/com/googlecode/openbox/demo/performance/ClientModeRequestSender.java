package com.googlecode.openbox.demo.performance;

import org.apache.http.impl.client.CloseableHttpClient;

import com.googlecode.openbox.demo.performance.client.DefaultDemoClient;
import com.googlecode.openbox.demo.performance.client.DemoClient;
import com.googlecode.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.googlecode.openbox.demo.performance.requests.DemoProxyResponse;
import com.googlecode.openbox.http.responses.JsonResponse;

public class ClientModeRequestSender extends AbstractRequestSender {

	private DemoClient client;

	public ClientModeRequestSender(CloseableHttpClient httpClient,
			int threadCount, String url, String dcName) {

		super(httpClient, threadCount, url, dcName);
		this.client = DefaultDemoClient.create(httpClient, getUrl());
		this.client.registerMonitor(getCyclicBarrierMonitor());
	}

	@Override
	public ClientServerDuration sendRequest(DemoProxyRequestParam param) {
		JsonResponse<DemoProxyResponse> response = client.sendProxyRequest(
				getDcName(), param);

		return new ClientServerDuration(response.getTimeLine(),
				Long.parseLong(response.getJsonObject().getDuration().trim()));

	}

}
