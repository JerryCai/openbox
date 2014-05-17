package com.googlecode.openbox.foo;

import com.googlecode.openbox.foo.auth.AuthFactory;
import com.googlecode.openbox.foo.auth.AuthType;
import com.googlecode.openbox.http.ExecutorMonitor;

public interface FooClientExtention {

	void registerProxy(FooClientProxy proxy);

	void unregisterProxy(FooClientProxy proxy);

	void registerMonitor(ExecutorMonitor monitor);

	void unregisterMonitor(ExecutorMonitor monitor);

	void cleanAllProxy();

	void addAuthType(AuthType authType);

	void setAuthType(AuthType authType);

	void setAuthBase(Object authBase);

	Object getAuthBase();

	void setAuthFactory(AuthFactory authFactory);

}
