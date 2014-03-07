package com.google.test.openbox.server;

import com.google.test.openbox.common.ExecuteResult;

public interface ServerGroup {
	
	void addServer(Server server);
	
	void addServers(ServerGroup serverGroup);
	
	int getNum();
	
	boolean isEmpty();
	
	Server getServer(String host);
	
	void removeServer(String host);
	
	void clearAll();
	
	Server[] listServers();
	
	String[] listServerHosts();
	
	ExecuteResult executeShell(String shell);
	
	ExecuteResult executeCommands(String command);
	
	String[] executeSingleCommandGetResponse(String command);
}
