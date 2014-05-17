package com.googlecode.openbox.server;

public interface SshRequest {
	Server getServer();
	
	String getCommand();

	void send(String sshCommand);

	String getResponse();

}
