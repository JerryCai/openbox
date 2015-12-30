package com.googlecode.openbox.server.log;

import com.googlecode.openbox.server.ssh.SshClient;

public abstract class AbstractServerLog implements ServerLog {
	public static final String PATH_SPLIT = "/";

	private volatile SshClient sshClient = null;

	private SshClient getSshClient() {
		if (null == sshClient) {
			synchronized (PATH_SPLIT) {
				if (null == sshClient) {
					sshClient = getServer().getSshClient();
				}
			}
		}
		return sshClient;
	}

	public String getPath() {
		return getHome() + PATH_SPLIT + getName();
	}

	public String getContentWithFullCommand(String fullCommand){
		return getSshClient().executeSingleCommand(System.out, fullCommand);
	}
	public String getContentByCommand(String command) {
		return getContentWithFullCommand(command+" "+getPath());
	}

	public int getCurrentLineNum() {
		String response = null;
		try {
			String path = getPath();
			response = getContentByCommand("wc -l ");
			response = response.replaceAll(path, "");
			return Integer.parseInt(response.trim());
		} catch (Exception e) {
			throw new RuntimeException(
					"get server log current line num failed as response is :"
							+ response);
		}
	}

	public String getLastestContent(int fromLineNum) {
		return getContentByCommand("sed -n '" + fromLineNum + ",$p' ");
	}

	public String getContentBetween(int beginLineNum, int endLineNum) {
		if (beginLineNum < 1) {
			throw new RuntimeException(
					"beginLineNum sould > 0 , but your beginLineNum is "
							+ beginLineNum);
		}
		if (beginLineNum > endLineNum) {
			throw new RuntimeException(
					"beginLineNum sould <= endLineNum , but is >");
		}
		return getContentByCommand("sed -n '" + beginLineNum + "," + endLineNum
				+ "p' ");
	}

	public String grepContentByKeys(String... keys) {
		String command = "";
		for (int i = 0; i < keys.length; i++) {
			if (i == 0) {
				command ="grep " + keys[i] +" "+ getPath();
			} else {
				command = command + " | grep " + keys[i];
			}
		}
		return getContentWithFullCommand(command);
	}

	public String grepContentByKeysFrom(int beginLineNum, String... keys) {
		String command = "sed -n '" + beginLineNum + ",$p' " +getPath();
		for (int i = 0; i < keys.length; i++) {
			if(null != keys[i] && !keys[i].trim().equals("")){
				command = command + " | grep " + keys[i];
			}
		}
		return getContentWithFullCommand(command);
	}

	public void deleteLog() {
		String command = "rm -rf ";
		getContentByCommand(command);
	}

	public void emptyLogByNewLogContent(String newLogContent) {
		String command = "echo '' > ";
		getContentByCommand(command);
	}
}
