package com.googlecode.openbox.server.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.trilead.ssh2.ChannelCondition;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

public class CommonSshClient extends DefaultSshClient {
	private static final Logger logger = LogManager.getLogger();

	public CommonSshClient(String ip, int port, String username, String password) {
		super(ip, port, username, password);
	}

	public static CommonSshClient newInstance(String ip, int port,
			String username, String password) {
		return new CommonSshClient(ip, port, username, password);
	}

	public Connection getConnection() throws IOException {
		Connection conn = new Connection(this.getIp(), this.getPort());
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(
				this.getUsername(), this.getPassword());
		if (!isAuthenticated) {
			throw new IOException("Authentication failed.");
		}
		if (logger.isInfoEnabled()) {
			logger.info("\nssh login success to ip=[" + this.getIp()
					+ "],port=[" + this.getPort() + "],username=["
					+ this.getUsername() + "],password=[*******]");
		}
		return conn;
	}

	public void executeCommand(OutputStream output, String command) {
		Connection conn = null;
		try {
			conn = getConnection();
		} catch (Exception e) {
			throw new SshException("create ssh session failed with ip=["
					+ this.getIp() + "],port=[" + this.getPort()
					+ "],username=[" + this.getUsername() + "],password=["
					+ this.getPassword() + "]", e);
		}
		Session session = null;
		String wrappedCommand = wrapperInput(command);
		try {
			session = conn.openSession();
			session.requestPTY("dumb");
			session.startShell();
			ExecutorService exec = Executors.newSingleThreadExecutor();
			Future<String> task = exec.submit(new OutputTask(session, output));
			PrintWriter out = new PrintWriter(session.getStdin());
			String commands[] = wrappedCommand.split("\n");
			for (int i = 0; i < commands.length; i++) {
				String cmd = commands[i];
				if ("".equals(cmd.trim()))
					continue;
				out.println(cmd);
			}
			out.println("exit 0");
			out.close();
			task.get();
			exec.shutdown();
			if (logger.isInfoEnabled()) {
				logger.info("exit status -->" + session.getExitStatus());
			}
		} catch (Exception e) {
			String msg = "execute commds=[" + wrappedCommand + "]failed !";
			throw new SshException(msg, e);
		} finally {
			if (null != session) {
				session.close();
			}
			if (null != conn) {
				conn.close();
			}
		}
	}

	class OutputTask implements Callable<String> {

		public String execute() throws IOException, InterruptedException {
			InputStream stdout = session.getStdout();
			InputStream stderr = session.getStderr();
			byte[] buffer = new byte[8192];
			while (true) {
				if ((stdout.available() == 0) && (stderr.available() == 0)) {
					int conditions = session.waitForCondition(
							ChannelCondition.STDERR_DATA
									| ChannelCondition.STDOUT_DATA
									| ChannelCondition.EXIT_STATUS, 600000);
					if ((conditions & ChannelCondition.TIMEOUT) != 0) {
						if (logger.isInfoEnabled()) {
							logger.info("wait timeout and exit now !");
						}
						break;
					}
					if ((conditions & ChannelCondition.EXIT_STATUS) != 0) {
						break;
					}
				}

				while (stdout.available() > 0) {
					int len = stdout.read(buffer);
					if (len > 0)
						output.write(buffer, 0, len);
				}

				while (stderr.available() > 0) {
					int len = stderr.read(buffer);
					if (len > 0)
						output.write(buffer, 0, len);
				}
			}
			if (logger.isInfoEnabled()) {
				logger.info("####################################");
			}
			return "SUCCESS";
		}

		public String call() throws Exception {
			Thread.sleep(2000);
			return execute();
		}

		private OutputStream output;
		private Session session;

		public OutputTask(Session session, OutputStream output) {
			super();
			this.session = session;
			this.output = output;
		}
	}
}
