package com.googlecode.openbox.server.log.monitor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.server.log.ServerLog;

public abstract class AbstractServerLogMonitor<T> implements
		ServerLogMonitor<T> {

	private static final Logger logger = LogManager.getLogger();

	private List<ServerLogHandler<T>> handlers;
	private Map<String, Integer> startLineNumbers;
	private List<ServerLogProvider> serverLogProviders;
	private List<ServerLog> serverLogs;

	public AbstractServerLogMonitor() {
		this.serverLogs = new LinkedList<ServerLog>();
		this.serverLogProviders = new LinkedList<ServerLogProvider>();
		this.handlers = new LinkedList<ServerLogHandler<T>>();
		this.startLineNumbers = new HashMap<String, Integer>();

		this.addServerLogHandler(new LogPrintHandler<T>());
	}

	@Override
	public void addServerLogProvider(ServerLogProvider serverLogProvider) {
		serverLogProviders.add(serverLogProvider);
	}

	@Override
	public void addServerLog(ServerLog serverLog) {
		serverLogs.add(serverLog);
	}

	@Override
	public AbstractServerLogMonitor<T> addServerLogHandler(
			ServerLogHandler<T> handler) {
		handlers.add(handler);
		return this;
	}

	@Override
	public T execute() throws Exception {
		init();
		start();
		T t = null;
		try {
			t = triggerActions();
		} finally {
			String logs = getMergedTriggerDuringLogs();
			verify(t, logs);
		}
		return t;
	}

	private void init() {
		collectServerLogsFromServerLogProvider();
	}

	private void collectServerLogsFromServerLogProvider() {
		for (ServerLogProvider serverLogProvider : serverLogProviders) {
			List<ServerLog> serverLogs = serverLogProvider.getServerLogs();
			if (null != serverLogs) {
				for (ServerLog serverLog : serverLogs) {
					addServerLog(serverLog);
				}
			}
		}
	}

	private void start() {
		for (ServerLog serverLog : serverLogs) {
			try {
				startLineNumbers.put(serverLog.toString(),
						serverLog.getCurrentLineNum());
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
	}

	private String getMergedTriggerDuringLogs() {
		String content = "";
		for (ServerLog serverLog : serverLogs) {
			try {
				TimeUnit.SECONDS.sleep(10);
				int startLineNum = startLineNumbers.get(serverLog.toString());
				content = content + "\n" + serverLog.getServer() + "\n"
						+ serverLog.getLastestContent(startLineNum);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
		}
		return content;

	}

	private void verify(T t, String logs) {
		for (ServerLogHandler<T> handler : handlers) {
			handler.action(t, logs);
		}
	}

}
