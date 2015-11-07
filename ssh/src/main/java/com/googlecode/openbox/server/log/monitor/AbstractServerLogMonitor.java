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

	private boolean enable;
	private List<ServerLogHandler<T>> handlers;
	private Map<String, Integer> startLineNumbers;
	private List<ServerLogProvider> serverLogProviders;
	private List<ServerLog> serverLogs;

	public AbstractServerLogMonitor(boolean enable) {
		this.enable = enable;
		this.serverLogs = new LinkedList<ServerLog>();
		this.serverLogProviders = new LinkedList<ServerLogProvider>();
		this.handlers = new LinkedList<ServerLogHandler<T>>();
		this.startLineNumbers = new HashMap<String, Integer>();

		this.addServerLogHandler(new LogPrintHandler<T>());
	}

	@Override
	public ServerLogMonitor<T> addServerLogProvider(
			ServerLogProvider serverLogProvider) {
		serverLogProviders.add(serverLogProvider);
		return this;
	}

	@Override
	public ServerLogMonitor<T> addServerLog(ServerLog serverLog) {
		serverLogs.add(serverLog);
		return this;
	}

	@Override
	public ServerLogMonitor<T> addServerLogHandler(ServerLogHandler<T> checker) {
		handlers.add(checker);
		return this;
	}

	@Override
	public T execute() throws Exception {
		if (this.enable) {
			return executeWithMonitorEnabled();
		}
		return triggerActions();
	}

	private T executeWithMonitorEnabled() throws Exception {
		init();
		start();
		T t = null;
		try {
			t = triggerActions();
		} finally {
			String logs = getMergedTriggerDuringLogs(t);
			verify(t, logs);
		}
		return t;
	}

	private void init() {
		cleanPreviousExecutedLogs();
		collectServerLogsFromServerLogProvider();
	}

	private void cleanPreviousExecutedLogs() {
		serverLogs.clear();
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

	private String getMergedTriggerDuringLogs(T t) {
		String content = "";
		for (ServerLog serverLog : serverLogs) {
			try {
				TimeUnit.SECONDS.sleep(10);
				int startLineNum = startLineNumbers.get(serverLog.toString());
				String[] logFilterKeys = getLogFilterKeys(t);
				String logs = null;
				if ((null == logFilterKeys) || (logFilterKeys.length <= 0)) {
					logs = serverLog.getLastestContent(startLineNum);
				} else {
					logs = serverLog.grepContentByKeysFrom(startLineNum,
							logFilterKeys);
				}
				content = content + "\n" + serverLog.getServer() + "\n" + logs;
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
