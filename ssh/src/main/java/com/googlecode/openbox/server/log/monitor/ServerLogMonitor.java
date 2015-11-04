package com.googlecode.openbox.server.log.monitor;

import com.googlecode.openbox.server.log.ServerLog;

public interface ServerLogMonitor<T> {

	void addServerLogProvider(ServerLogProvider serverLogProvider);

	void addServerLog(ServerLog serverLog);

	ServerLogMonitor<T> addServerLogHandler(ServerLogHandler<T> checker);

	T triggerActions() throws Exception;

	T execute() throws Exception;

}
