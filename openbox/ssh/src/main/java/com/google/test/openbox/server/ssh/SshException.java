package com.google.test.openbox.server.ssh;

public class SshException extends RuntimeException {

	public static final String EXCEPTION_FROM = "[GSSH - SFTP]  Exception ";

	private static final long serialVersionUID = 1L;

	public SshException(String message) {
		super(EXCEPTION_FROM + message);
	}

	public SshException(String message, Throwable cause) {
		super(EXCEPTION_FROM + message, cause);
	}

	public SshException(Throwable cause) {
		super(EXCEPTION_FROM + cause.getMessage(), cause);
	}
}
