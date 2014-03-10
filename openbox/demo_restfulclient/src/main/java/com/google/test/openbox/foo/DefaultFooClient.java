package com.google.test.openbox.foo;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.test.openbox.foo.request.addfoo.AddFooParam;
import com.google.test.openbox.foo.request.addfoo.AddFooRequest;
import com.google.test.openbox.foo.request.addfoo.AddFooResponse;
import com.google.test.openbox.foo.request.addfoo.AddFooResponseHandler;
import com.google.test.openbox.foo.request.deletefoo.DeleteFooRequest;
import com.google.test.openbox.foo.request.deletefoo.DeleteFooResponse;
import com.google.test.openbox.foo.request.deletefoo.DeleteFooResponseHandler;
import com.google.test.openbox.foo.request.getfoo.GetFooParam;
import com.google.test.openbox.foo.request.getfoo.GetFooRequest;
import com.google.test.openbox.foo.request.getfoo.GetFooResponse;
import com.google.test.openbox.foo.request.getfoo.GetFooResponseHandler;
import com.google.test.openbox.http.DefaultHttpExecutor;
import com.google.test.openbox.http.HttpExecutor;
import com.google.test.openbox.http.responses.JsonResponse;

public class DefaultFooClient extends AbstractFooClient {

	private String url;
	private ClientVersion version;

	public static final String DEFAULT_USER_AGENT = "MeetingClient-"
			+ ClientVersion.V1;

	private DefaultFooClient(CloseableHttpClient httpClient, String url,
			ClientVersion version) {
		super(httpClient);
		this.url = url;
		this.version = version;
	}

	public static DefaultFooClient newInstance(CloseableHttpClient httpClient,
			String url, ClientVersion version) {
		return new DefaultFooClient(httpClient, url, version);

	}

	@Override
	public JsonResponse<AddFooResponse> addFoo(AddFooParam params) {

		AddFooRequest request = new AddFooRequest(url, version, params);

		HttpExecutor<JsonResponse<AddFooResponse>> httpExecutor = new DefaultHttpExecutor<JsonResponse<AddFooResponse>>(
				getHttpClient(), request, AddFooResponseHandler.newInstance());

		try {
			registerAllPlugins(httpExecutor);
			httpExecutor.execute();
			return httpExecutor.getResponse();
		} catch (Exception e) {
			throw FooClientException.create(e);
		}
	}

	@Override
	public JsonResponse<GetFooResponse> getFoo(GetFooParam params) {
		GetFooRequest request = new GetFooRequest(url, version,
				params.getFooId());
		String password = params.getPassword();
		if (null != password) {
			request.setFooPassword(password);
		}
		HttpExecutor<JsonResponse<GetFooResponse>> httpExecutor = new DefaultHttpExecutor<JsonResponse<GetFooResponse>>(
				getHttpClient(), request,
				GetFooResponseHandler.newInstance());

		try {
			registerAllPlugins(httpExecutor);
			httpExecutor.execute();
			return httpExecutor.getResponse();
		} catch (Exception e) {
			throw FooClientException.create(e);
		}
	}

	@Override
	public JsonResponse<DeleteFooResponse> deleteFoo(String id) {
		DeleteFooRequest request = new DeleteFooRequest(url, version, id);

		HttpExecutor<JsonResponse<DeleteFooResponse>> httpExecutor = new DefaultHttpExecutor<JsonResponse<DeleteFooResponse>>(
				getHttpClient(), request,
				DeleteFooResponseHandler.newInstance());

		try {
			registerAllPlugins(httpExecutor);
			httpExecutor.execute();
			return httpExecutor.getResponse();
		} catch (Exception e) {
			throw FooClientException.create(e);
		}
	}

}
