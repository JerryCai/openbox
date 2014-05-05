package com.googlecode.openbox.demo.performance.client;

import com.googlecode.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.googlecode.openbox.demo.performance.requests.DemoProxyResponse;
import com.googlecode.openbox.http.extention.AppClientExtendable;
import com.googlecode.openbox.http.responses.JsonResponse;

public interface DemoClient extends AppClientExtendable {

	JsonResponse<DemoProxyResponse> sendProxyRequest(String dcName,
			DemoProxyRequestParam param);

}
