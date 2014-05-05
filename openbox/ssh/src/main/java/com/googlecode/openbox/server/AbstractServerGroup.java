package com.googlecode.openbox.server;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractServerGroup implements ServerGroup {

	private final Map<String, Server> serverGroup;

	public AbstractServerGroup() {
		this.serverGroup = new HashMap<String, Server>();
	}

	@Override
	public void addServer(Server server) {
		serverGroup.put(server.getIp(), server);
	}
	

	@Override
	public void addServers(ServerGroup serverGroup) {
		Server[] servers = serverGroup.listServers();
		for(Server server: servers){
			addServer(server);
		}
	}

	@Override
	public int getNum() {
		return serverGroup.size();
	}

	@Override
	public boolean isEmpty() {
		return serverGroup.isEmpty();
	}

	@Override
	public Server getServer(String host) {
		return serverGroup.get(host);
	}

	@Override
	public void removeServer(String host) {
		serverGroup.remove(host);
	}

	@Override
	public void clearAll() {
		serverGroup.clear();
	}

	@Override
	public Server[] listServers() {
		return serverGroup.values().toArray(new Server[0]);
	}

	@Override
	public String[] listServerHosts() {
		Server[] servers = listServers();
		int num = servers.length;
		String[] serverHosts = new String[num];
		for (int i = 0; i < num; i++) {
			serverHosts[i] = servers[i].getIp();
		}
		return serverHosts;
	}
}
