package com.google.test.openbox.demo.performance.requests;

import org.apache.http.client.methods.HttpPost;

import com.google.test.openbox.http.requests.JsonBodyRequest;

public class DemoProxyRequest extends JsonBodyRequest<DemoProxyRequestParam> {

	public static final String HEAD_NAME_DCNAME = "DCName";
	public static final String REQUEST_PATH="/proxy/concurrency/demo";
	private DemoProxyRequestParam requestBody;

	public DemoProxyRequest(String url, String dcName,
			DemoProxyRequestParam requestBody) {
		super(url);
		addHeader(HEAD_NAME_DCNAME, dcName);
		this.requestBody = requestBody;
		this.getURIBuilder().setPath(REQUEST_PATH);
	}

	@Override
	public String getMethod() {
		return HttpPost.METHOD_NAME;
	}

	@Override
	public DemoProxyRequestParam getJsonObjectBody() {
		return requestBody;
	}

}
