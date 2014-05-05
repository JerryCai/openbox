package com.googlecode.openbox.testu.userpool;

import java.io.File;
import java.util.List;

import com.googlecode.openbox.jsonpool.JsonStoreProvider;

class JsonStoreProviderAdaptor<T> implements JsonStoreProvider<DC, T> {
	private static final String USER_POOL_STORE_FOLDER = "/users/";
	private static final String USER_FILE_POSTFIX = "_users.txt";

	private UserProvider<T> userProvider;

	public JsonStoreProviderAdaptor(UserProvider<T> userProvider) {
		this.userProvider = userProvider;
	}

	@Override
	public List<T> get(DC dc, int userNum, int fromIndex) {
		return userProvider.getUser(dc, userNum, fromIndex);
	}

	@Override
	public int getInitSize() {
		return userProvider.getInitUserNum();
	}

	@Override
	public String getStorePath(DC k) {
		return getUserStorePath(k);
	}

	@Override
	public Class<T> getJsonClass() {
		return userProvider.getUserClass();
	}

	private String getUserStorePath(DC dc) {
		return new File(".").getAbsolutePath() + USER_POOL_STORE_FOLDER
				+ dc.getDcName() + "_DC" + USER_FILE_POSTFIX;
	}

}
