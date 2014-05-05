package com.googlecode.openbox.foo.auth;

import com.googlecode.openbox.foo.FooClientProxy;

public interface AuthStrategy {

	AuthType getAutyType();

	FooClientProxy getAuthStrategyProxy(final Object authBase);

}
