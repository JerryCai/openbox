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

	public String getContentByCommand(String command) {
		return getSshClient().executeSingleCommand(System.out, command+" "+getPath());
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
				command = command + "grep " + keys[i] + " ";
			} else {
				command = command + " | grep " + keys[i];
			}
		}
		return getContentByCommand(command);
	}

	public String grepContentByKeysFrom(int beginLineNum, String... keys) {
		String command = "sed -n '" + beginLineNum + ",$p' ";
		for (int i = 0; i < keys.length; i++) {
			command = command + " | grep " + keys[i];
		}
		return getContentByCommand(command);
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
