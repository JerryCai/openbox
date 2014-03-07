package com.google.test.openbox.server;

import com.google.test.openbox.server.ssh.CommonSshClient;
import com.google.test.openbox.server.ssh.SshClient;

public class Server {

	private String ip;
	private int sshPort;
	private String username;
	private String password;

	public Server(String ip, int sshPort, String username, String password) {
		this.ip = ip;
		this.sshPort = sshPort;
		this.username = username;
		this.password = password;
	}

	public static Server newInstance(String ip, int sshPort, String username,
			String password) {
		return new Server(ip, sshPort, username, password);
	}

	public SshClient getSshClient() {
		return getSshClient(false);
	}
	
	public SshClient getSshClient(boolean needCmdWrapper) {
		CommonSshClient sshClient = CommonSshClient.newInstance(getIp(), getSshPort(),
				getUsername(), getPassword());
		sshClient.setNeedSource(needCmdWrapper);
		return sshClient;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getSshPort() {
		return sshPort;
	}

	public void setSshPort(int sshPort) {
		this.sshPort = sshPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString() {
		return "{ ip=[" + ip + "],sshPort=[" + sshPort + "],username=["
				+ username + "],password=[" + password + "] }";
	}

}
