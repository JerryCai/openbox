package com.googlecode.openbox.server.loaders;

import com.googlecode.openbox.common.IOUtils;

public class IpListFileServerGroupLoader extends AbstractServerGroupLoader {

	private String ipLineContext;

	private IpListFileServerGroupLoader(String username, String password,
			int port, String projectRelativePath) {
		super(username, password, port);
		String path = getClass().getClassLoader().getResource(projectRelativePath).getFile();
		this.ipLineContext = IOUtils.getStringFromFile(path);
	}

	public static IpListFileServerGroupLoader newInstance(String username,
			String password, int port, String filePath) {
		return new IpListFileServerGroupLoader(username, password, port,
				filePath);
	}

	@Override
	public String[] getIps() {
		return ipLineContext.split("(\\r|\\n)");
	}

}
