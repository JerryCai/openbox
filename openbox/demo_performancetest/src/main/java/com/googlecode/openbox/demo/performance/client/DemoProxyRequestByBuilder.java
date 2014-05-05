package com.googlecode.openbox.demo.performance.client;


import com.googlecode.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.googlecode.openbox.http.GsonFactory;
import com.googlecode.openbox.http.httpbuilder.HttpBuilder;
import com.googlecode.openbox.http.httpbuilder.HttpBuilder.Response;

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
