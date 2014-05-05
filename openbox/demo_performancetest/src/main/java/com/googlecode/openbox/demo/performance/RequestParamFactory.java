package com.googlecode.openbox.demo.performance;

import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.demo.performance.requests.DemoProxyRequestParam;

public class RequestParamFactory {
	private RequestParamFactory() {
	}

	public static DemoProxyRequestParam createDemoProxyRequestParam(int size) {
		DemoProxyRequestParam param = new DemoProxyRequestParam();
		param.setDescription(UtilsAPI.getRepeatString(".", size));
		return param;
	}
}
