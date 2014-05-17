package com.googlecode.openbox.server;

public class DefaultSshRequest implements SshRequest {
	private Server server;
	private static final ThreadLocal<String> commandPool = new ThreadLocal<String>();
	private static final ThreadLocal<String> resposnePool = new ThreadLocal<String>();

	public DefaultSshRequest(Server server){
		this.server = server;
	}
	
	@Override
	public void send(String sshCommand) {
		commandPool.set(sshCommand);
		resposnePool.set(server.getSshClient(false).executeSingleCommand(
				System.out, commandPool.get()));
	}

	public Server getServer() {
		return server;
	}

	public String getCommand() {
		return commandPool.get();
	}

	@Override
	public String getResponse() {
		return resposnePool.get();

	}
}
