package com.google.test.openbox.demo.performance.client;


import com.google.test.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.google.test.openbox.http.GsonFactory;
import com.google.test.openbox.http.httpbuilder.HttpBuilder;
import com.google.test.openbox.http.httpbuilder.HttpBuilder.Response;

public class DemoProxyRequestByBuilder {
	public static final String HEADERNAME_DCNAME = "DCName";

	private String url;

	public DemoProxyRequestByBuilder(String url) {
		this.url = url;
	}

	public Response sendProxyRequest(String dcName, DemoProxyRequestParam param) {

		return HttpBuilder.create()
				.setUrl(url)
				.addHeader(HEADERNAME_DCNAME, dcName)
				.setText(GsonFactory.createGson().toJson(param)).execute();
	}

}
