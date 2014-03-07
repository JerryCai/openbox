package com.google.test.openbox.demo.performance;

import com.google.test.openbox.common.UtilsAPI;
import com.google.test.openbox.demo.performance.requests.DemoProxyRequestParam;

public class RequestParamFactory {
	private RequestParamFactory() {
	}

	public static DemoProxyRequestParam createDemoProxyRequestParam(int size) {
		DemoProxyRequestParam param = new DemoProxyRequestParam();
		param.setDescription(UtilsAPI.getRepeatString(".", size));
		return param;
	}
}
