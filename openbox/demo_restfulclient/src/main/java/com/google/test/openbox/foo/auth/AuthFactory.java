package com.google.test.openbox.foo.auth;

public interface AuthFactory {
	
	public AuthStrategy getAuthStrategy(AuthType authType);
}
