package com.googlecode.openbox.server;

import com.googlecode.openbox.common.ExecuteResult;

public class SequenceServerGroup extends AbstractServerGroup {

	public SequenceServerGroup() {
		super();
	}

	public static SequenceServerGroup newInstance() {
		return new SequenceServerGroup();
	}

	@Override
	public ExecuteResult executeShell(String shell) {
		Server[] servers = listServers();
		ExecuteResult executeResult = ExecuteResult.newInstance(true);
		for (Server server : servers) {
			try {
				server.getSshClient().executeShellByFTP(System.out, shell);
				executeResult.appendMessage("execute shell on server :"
						+ server + " success !");
			} catch (Exception e) {
				executeResult.setResult(false);
				executeResult.appendMessage("execute shell on server :"
						+ server + " failed !");
			}
		}
		return executeResult;
	}

	@Override
	public ExecuteResult executeCommands(String command) {
		Server[] servers = listServers();
		ExecuteResult executeResult = ExecuteResult.newInstance(true);
		for (Server server : servers) {
			try {
				server.getSshClient().executeCommand(System.out, command);
				executeResult.appendMessage("execute command on server :"
						+ server + " success !");
			} catch (Exception e) {
				executeResult.setResult(false);
				executeResult.appendMessage("execute command on server :"
						+ server + " failed !");
			}
		}
		return executeResult;
	}

	@Override
	public String[] executeSingleCommandGetResponse(String command) {
		Server[] servers = listServers();
		int num = servers.length;
		String[] responses = new String[num];
		ExecuteResult executeResult = ExecuteResult.newInstance(true);
		for (int i = 0; i < num; i++) {
			Server server = servers[i];
			try {
				responses[i] = server.getSshClient().executeSingleCommand(
						System.out, command);
				executeResult.appendMessage("execute command on server :"
						+ server + " success !");
			} catch (Exception e) {
				executeResult.setResult(false);
				executeResult.appendMessage("execute command on server :"
						+ server + " failed !");
			}
		}
		return responses;
	}
}
