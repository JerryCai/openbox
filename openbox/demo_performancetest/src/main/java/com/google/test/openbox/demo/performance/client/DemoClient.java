package com.google.test.openbox.demo.performance.client;

import com.google.test.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.google.test.openbox.demo.performance.requests.DemoProxyResponse;
import com.google.test.openbox.http.extention.AppClientExtendable;
import com.google.test.openbox.http.responses.JsonResponse;

public interface DemoClient extends AppClientExtendable {

	JsonResponse<DemoProxyResponse> sendProxyRequest(String dcName,
			DemoProxyRequestParam param);

}
