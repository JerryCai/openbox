package com.google.test.openbox.demo.performance;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.test.openbox.demo.performance.client.DefaultDemoClient;
import com.google.test.openbox.demo.performance.client.DemoClient;
import com.google.test.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.google.test.openbox.demo.performance.requests.DemoProxyResponse;
import com.google.test.openbox.http.responses.JsonResponse;

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
