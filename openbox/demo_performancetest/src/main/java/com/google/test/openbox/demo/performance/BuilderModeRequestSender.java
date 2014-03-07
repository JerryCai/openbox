package com.google.test.openbox.demo.performance;


import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

import com.google.gson.Gson;
import com.google.test.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.google.test.openbox.demo.performance.requests.DemoProxyResponse;
import com.google.test.openbox.http.GsonFactory;
import com.google.test.openbox.http.httpbuilder.HttpBuilder;
import com.google.test.openbox.http.httpbuilder.HttpBuilder.Response;

public class BuilderModeRequestSender extends AbstractRequestSender {

	public BuilderModeRequestSender(CloseableHttpClient httpClient,
			int threadCount, String url, String dcName) {

		super(httpClient, threadCount, url, dcName);
	}

	@Override
	public ClientServerDuration sendRequest(DemoProxyRequestParam param) {
		HttpBuilder httpBuilder = HttpBuilder.create(getHttpClient());
		httpBuilder.getExecutorMonitorManager().register(
				getCyclicBarrierMonitor());
		httpBuilder.setMethod("POST")
				.setUrl(getUrl() + "/proxy/concurrency/demo")
				.addHeader("DCName", getDcName())
				.setText(GsonFactory.createGson().toJson(param))
				.setContentEncoding("UTF-8")
				.setContentType(ContentType.APPLICATION_JSON);
		Response response = httpBuilder.execute();

		DemoProxyResponse demoProxyResponse = new Gson().fromJson(
				response.getContent(), DemoProxyResponse.class);

		return new ClientServerDuration(response.getTimeLine(),
				Long.parseLong(demoProxyResponse.getDuration().trim()));

	}

}
