package com.googlecode.openbox.server.log;

import com.googlecode.openbox.server.Server;

public class ServerLogImpl extends AbstractServerLog {
	private Server server;
	private String home;
	private String name;

	public ServerLogImpl(Server server, String home, String name) {
		this.server = server;
		this.home = home;
		this.name = name;
	}

	public static ServerLogImpl newInstance(Server server, String home,
			String name) {
		return new ServerLogImpl(server, home, name);
	}

	public Server getServer() {
		return server;
	}

	public String getHome() {
		return home;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return "log info : home=[" + home + "],name=[" + name + "] on servr "
				+ server;
	}
}
