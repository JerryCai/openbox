package com.googlecode.openbox.demo.performance.client;

import org.apache.http.impl.client.CloseableHttpClient;

import com.googlecode.openbox.demo.performance.requests.DemoProxyRequest;
import com.googlecode.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.googlecode.openbox.demo.performance.requests.DemoProxyResponse;
import com.googlecode.openbox.demo.performance.requests.DemoProxyResponseHandler;
import com.googlecode.openbox.http.DefaultHttpExecutor;
import com.googlecode.openbox.http.HttpExecutor;
import com.googlecode.openbox.http.extention.ExtendedAppClient;
import com.googlecode.openbox.http.responses.JsonResponse;

public class DefaultDemoClient extends ExtendedAppClient implements DemoClient {
	private String url;

	private DefaultDemoClient(CloseableHttpClient httpClient, String url) {
		super(httpClient);
		this.url = url;
	}

	public static DefaultDemoClient create(CloseableHttpClient httpClient,
			String url) {
		return new DefaultDemoClient(httpClient, url);
	}

	@Override
	public JsonResponse<DemoProxyResponse> sendProxyRequest(String dcName,
			DemoProxyRequestParam param) {

		DemoProxyRequest request = new DemoProxyRequest(url, dcName, param);

		HttpExecutor<JsonResponse<DemoProxyResponse>> httpExecutor = new DefaultHttpExecutor<JsonResponse<DemoProxyResponse>>(
				this.getHttpClient(), request,
				DemoProxyResponseHandler.newInstance());

		try {
			registerAllPlugins(httpExecutor);
			httpExecutor.execute();
			return httpExecutor.getResponse();
		} catch (Exception e) {
			throw DemoClientException.create(e);
		}
	}

}
