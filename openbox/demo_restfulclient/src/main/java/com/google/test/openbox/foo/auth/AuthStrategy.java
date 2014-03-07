package com.google.test.openbox.foo.auth;

import com.google.test.openbox.foo.FooClientProxy;

public interface AuthStrategy {

	AuthType getAutyType();

	FooClientProxy getAuthStrategyProxy(final Object authBase);

}
