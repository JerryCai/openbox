package com.googlecode.openbox.server;

import java.util.Map;


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

	Map<Server,String>  executeShell(String shell);

	Map<Server,String[]>  executeCommands(String commands);

	Map<Server,String> executeSingleCommandGetResponse(String command);

	public interface ServerAction<T> {
		T access(Server server);
	}

	<T> Map<Server, T> visit(ServerAction<T> action) ;

	public interface ServerHandler {
		void execute(Server server);
	}

	void visit(ServerHandler serverHandler) ;

}
