package com.googlecode.openbox.testu.userpool;

import java.util.List;

import com.googlecode.openbox.jsonpool.JsonStorePool;
import com.googlecode.openbox.jsonpool.JsonStorePoolImpl;

public class UserPoolImpl<T> implements UserPool<T> {

	private JsonStorePool<DC, T> userpool;

	public UserPoolImpl(UserProvider<T> userProvider) {
		this.userpool = new JsonStorePoolImpl<DC, T>(
				new JsonStoreProviderAdaptor<T>(userProvider));
	}

	@Override
	public T borrowUser(DC dc) {
		return userpool.get(dc);
	}

	@Override
	public List<T> borrowUsers(DC dc, int num) {
		return userpool.get(dc, num);
	}

	@Override
	public void returnUser(DC dc, T user) {
		userpool.back(dc, user);
	}

	@Override
	public void returnUsers(DC dc, List<T> users) {
		userpool.back(dc, users);
	}
}
