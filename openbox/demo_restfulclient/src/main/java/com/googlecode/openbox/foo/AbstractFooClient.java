package com.googlecode.openbox.foo;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;

import com.googlecode.openbox.foo.auth.AuthFactory;
import com.googlecode.openbox.foo.auth.AuthType;
import com.googlecode.openbox.http.HttpExecutor;
import com.googlecode.openbox.http.extention.ExtendedAppClient;

public abstract class AbstractFooClient extends ExtendedAppClient implements
		FooClient {

	private List<AuthType> authTypes;

	private Object authBase;
	private AuthFactory authFactory;

	public AbstractFooClient(CloseableHttpClient httpClient) {
		super(httpClient);
		this.authTypes = new LinkedList<AuthType>();
	}

	@Override
	public void registerProxy(FooClientProxy proxy) {
		super.registerProxy(proxy);
	}

	@Override
	public void unregisterProxy(FooClientProxy proxy) {
		super.unregisterProxy(proxy);

	}

	@Override
	public void addAuthType(AuthType authType) {
		authTypes.add(authType);
	}

	@Override
	public void setAuthType(AuthType authType) {
		authTypes.clear();
		authTypes.add(authType);
	}

	@Override
	public void setAuthBase(Object authBase) {
		this.authBase = authBase;
	}

	@Override
	public Object getAuthBase() {
		return this.authBase;
	}

	@Override
	public void setAuthFactory(AuthFactory authFactory) {
		this.authFactory = authFactory;
	}

	public void registerAllPlugins(final HttpExecutor<?> httpExecutor) {
		// firstly, register auth  if exist
		if (null != authFactory) {
			for (AuthType authType : authTypes) {
				FooClientProxy authStrategyProxy = authFactory
						.getAuthStrategy(authType).getAuthStrategyProxy(
								getAuthBase());
				registerProxy(authStrategyProxy);
			}
		}
		//then call extention to register to httpExecutor
		super.registerAllPlugins(httpExecutor);



	}
}
