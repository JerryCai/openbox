package com.googlecode.openbox.foo.auth;

public interface AuthFactory {
	
	public AuthStrategy getAuthStrategy(AuthType authType);
}
