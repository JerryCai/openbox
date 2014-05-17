package com.googlecode.openbox.server.loaders;

public class TestLineServerGroupLoader extends AbstractServerGroupLoader {

	private String ipLineContext;

	private TestLineServerGroupLoader(String username, String password,
			int port, String ipLineContext) {
		super(username, password, port);
		this.ipLineContext = ipLineContext;
	}

	public static TestLineServerGroupLoader newInstance(String username,
			String password, int port, String ipLineContext) {
		return new TestLineServerGroupLoader(username, password, port,
				ipLineContext);
	}

	@Override
	public String[] getIps() {
		return ipLineContext.split("(\\r|\\n)");
	}

}
