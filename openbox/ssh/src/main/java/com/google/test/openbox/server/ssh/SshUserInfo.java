package com.google.test.openbox.server.ssh;

import java.util.logging.Logger;

import com.jcraft.jsch.UserInfo;

public class SshUserInfo implements UserInfo {
	private static final Logger LOGGER = Logger.getLogger(SshUserInfo.class.getName());
	
	String password;
	String passphrase;
	
	public SshUserInfo(String password){
		this.password=password;
		this.passphrase=password;
		
	}
	public String getPassphrase() {
		return passphrase;
	}

	public String getPassword() {
		return password;
	}

	public boolean promptPassword(String message) {
		return false;
	}

	public boolean promptPassphrase(String message) {
		return false;
	}

	public boolean promptYesNo(String message) {
		return false;
	}

	public void showMessage(String message) {
		LOGGER.info(message);
	}
}
