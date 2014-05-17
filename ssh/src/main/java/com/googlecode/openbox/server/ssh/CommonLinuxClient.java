package com.googlecode.openbox.server.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.trilead.ssh2.ChannelCondition;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

public class CommonLinuxClient {
	private static final Logger logger = LogManager.getLogger();

	private String ip;
	private int port;
	private String username;
	private String password;
	private Connection conn;
	private Session session;
	private PrintWriter out;
	private BufferedReader stdoutReader;
	private BufferedReader stderrReader;

	public CommonLinuxClient(String ip, int port, String username,
			String password) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		initConnection();
		initSession();
	}

	public static CommonLinuxClient newInstance(String ip, int port,
			String username, String password) {
		return new CommonLinuxClient(ip, port, username, password);
	}

	private void initConnection() {
		try {
			conn = new Connection(ip, port);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username,
					password);
			if (!isAuthenticated) {
				throw new IOException("Authentication failed.");
			}
			if (logger.isInfoEnabled()) {
				logger.info("\nssh login success to ip=[" + ip + "],port=["
						+ port + "],username=[" + username
						+ "],password=[*******]");
			}
		} catch (Exception e) {
			String msg = "\nSSH Connection and Login error to ip=[" + ip
					+ "],port=[" + port + "],username=[" + username
					+ "],password=[*******]";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	private void initSession() {
		try {
			session = conn.openSession();
			session.requestDumbPTY();
			session.startShell();
			stdoutReader = new BufferedReader(new InputStreamReader(
					session.getStdout()));
			stderrReader = new BufferedReader(new InputStreamReader(
					session.getStderr()));

			out = new PrintWriter(session.getStdin());
		} catch (Exception e) {
			String msg = "\nOpen SSH2 Session Error !";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}

	}

	public String execute(String command) {

		out.println(command);
		StringBuilder sb = new StringBuilder();
		try {
			char[] buffer = new char[2048];
			int stdOutConditions = session.waitForCondition(
					ChannelCondition.STDOUT_DATA
							| ChannelCondition.STDERR_DATA
							| ChannelCondition.EXIT_STATUS | ChannelCondition.EOF |ChannelCondition.EXIT_SIGNAL |ChannelCondition.TIMEOUT, 3000);
			if ((stdOutConditions & ChannelCondition.STDOUT_DATA) != 0) {
				int len = stdoutReader.read(buffer);
				if (len > 0) {
					sb.append(buffer, 0, len);
				}
			}else if ((stdOutConditions & ChannelCondition.STDERR_DATA) != 0) {
				int errLen = stderrReader.read(buffer);
				if (errLen > 0) {
					sb.append(buffer, 0, errLen);
				}

			}
			String response = sb.toString();
			if (logger.isInfoEnabled()) {
				logger.info("\n" + response);
			}
			return response;

		} catch (Exception e) {
			throw new RuntimeException("execute comand [" + command
					+ "] error !", e);
		}

	}

	public void close() {

		if (null != stdoutReader) {
			try {
				stdoutReader.close();
			} catch (IOException e) {
			}
		}
		if (null != stderrReader) {
			try {
				stderrReader.close();
			} catch (IOException e) {
			}
		}
		if (null != out) {
			out.close();
		}
		if (null != session) {
			session.close();
		}
		if (null != conn) {
			conn.close();
		}
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
