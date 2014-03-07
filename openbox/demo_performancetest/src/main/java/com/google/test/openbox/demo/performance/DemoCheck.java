package com.google.test.openbox.demo.performance;

import org.apache.http.entity.ContentType;

import com.google.test.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.google.test.openbox.http.GsonFactory;
import com.google.test.openbox.http.httpbuilder.HttpBuilder;

public class DemoCheck {

	public static void main(String... args) {
		DemoProxyRequestParam param = RequestParamFactory
				.createDemoProxyRequestParam(20);
		param.setId("jerry-id-0002");

		HttpBuilder.create().setMethod("POST")
				.setUrl("http://10.79.154.32:8999/proxy/concurrency/meeting")
				.addHeader("DCName", "dc2")
				.setText(GsonFactory.createGson().toJson(param))
				.setContentEncoding("UTF-8")
				.setContentType(ContentType.APPLICATION_JSON).execute();
	}

}
