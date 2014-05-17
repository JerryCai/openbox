package com.googlecode.openbox.server.loaders;

import com.googlecode.openbox.server.ConcurrentServerGroup;
import com.googlecode.openbox.server.SequenceServerGroup;
import com.googlecode.openbox.server.Server;
import com.googlecode.openbox.server.ServerGroup;

public abstract class AbstractServerGroupLoader implements ServerGroupLoader {
	private String username;
	private String password;
	private int port;

	public AbstractServerGroupLoader(String username, String password, int port) {
		this.username = username;
		this.password = password;
		this.port = port;

	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public ServerGroup toServerGroup(boolean isConcurrent) {

		ServerGroup sg = null;
		if (isConcurrent) {
			sg = ConcurrentServerGroup.newInstance();
		} else {
			sg = SequenceServerGroup.newInstance();
		}
		String[] ips = getIps();
		for (String ip : ips) {
			Server server = Server.newInstance(ip, getPort(), getUsername(),
					getPassword());
			sg.addServer(server);
		}
		return sg;
	}

}
