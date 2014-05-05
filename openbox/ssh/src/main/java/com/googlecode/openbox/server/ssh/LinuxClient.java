package com.googlecode.openbox.server.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.trilead.ssh2.ChannelCondition;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

public class LinuxClient {
	private static final Logger logger = LogManager.getLogger();

	private String ip;
	private int port;
	private String username;
	private String password;
	private Connection conn;

	public LinuxClient(String ip, int port, String username, String password) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		initConnection();
	}

	public static LinuxClient newInstance(String ip, int port, String username,
			String password) {
		return new LinuxClient(ip, port, username, password);
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

	private Session getSession() {
		try {
			Session session = conn.openSession();
			session.requestPTY("dumb");
			session.startShell();
			return session;
			
		} catch (Exception e) {
			String msg = "\nOpen SSH2 Session Error !";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}

	}

	public String executeCommand(String command) {
		String response = null;
		Session session = getSession();
		PrintWriter out = new PrintWriter(session.getStdin());
		try {
			ExecutorService exec = Executors.newSingleThreadExecutor();
			Future<String> task = exec.submit(new SshTask(session));
			String commands[] = command.split("\n");
			for (int i = 0; i < commands.length; i++) {
				String cmd = commands[i];
				if ("".equals(cmd.trim()))
					continue;
				out.println(cmd);
			}
			out.println("exit 0");
			out.println("exit 0");
			out.close();
			response = task.get();
			exec.shutdown();
			exec.awaitTermination(2, TimeUnit.DAYS);
			logger.info("exit status -->" + session.getExitStatus());
		} catch (Exception e) {
			String msg = "execute commd=[" + command + "]failed !";
			throw new RuntimeException(msg, e);
		}finally{
			out.close();
			session.close();
		}
		return response;
	}


	public void close() {
		if (null != conn) {
			conn.close();
		}
	}

	private class SshTask implements Callable<String> {

		public String execute() throws IOException, InterruptedException {
			InputStream stdout = session.getStdout();
			InputStream stderr = session.getStderr();
			char[] buffer = new char[2048];
			BufferedReader stdoutReader = new BufferedReader(
					new InputStreamReader(stdout));
			BufferedReader stderrReader = new BufferedReader(
					new InputStreamReader(stderr));

			StringBuilder sb = new StringBuilder();

			while (true) {
				if ((stdout.available() == 0) && (stderr.available() == 0)) {
					int conditions = session.waitForCondition(
							ChannelCondition.STDERR_DATA
									| ChannelCondition.STDOUT_DATA
									| ChannelCondition.EXIT_STATUS, 600000);
					if ((conditions & ChannelCondition.TIMEOUT) != 0) {
						logger.info("wait timeout and exit now !");
						break;
					}
					if ((conditions & ChannelCondition.EXIT_STATUS) != 0) {
						break;
					}
				}
				int len = -1;
				do{
					len =stdoutReader.read(buffer);
					if (len > 0) {
						sb.append(buffer, 0, len);
					}
				}while(-1 != len);

				int errLen= -1;
				while (-1 != (errLen = stderrReader.read(buffer))) {
					if (errLen > 0) {
						sb.append(buffer, 0, errLen);
					}
				}
			}
			stdout.close();
			stderr.close();
			String response = sb.toString();
			if (logger.isInfoEnabled()) {
				logger.info("\n" + response);
			}
			return response;
		}

		public String call() throws Exception {
			return execute();
		}

		private Session session;

		public SshTask(Session session) {
			super();
			this.session = session;
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
	
	
	public static void main(String ...args){
		LinuxClient linuxClient= LinuxClient.newInstance("10.224.57.21", 22, "wbxroot", "wbx@AaR00t");
		String response = linuxClient.executeCommand("sudo su -\n ls /usr/ -rt");
		response = linuxClient.executeCommand("ls -lrt");
		System.out.print("response is -->"+ response);
		linuxClient.close();
	}
}
