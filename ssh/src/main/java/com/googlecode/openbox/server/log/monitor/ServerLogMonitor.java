package com.googlecode.openbox.server.log.monitor;

import com.googlecode.openbox.server.log.ServerLog;

public interface ServerLogMonitor<T> {

	ServerLogMonitor<T> addServerLogProvider(ServerLogProvider serverLogProvider);

	ServerLogMonitor<T> addServerLog(ServerLog serverLog);

	ServerLogMonitor<T> addServerLogHandler(ServerLogHandler<T> handler);
	
	ServerLogMonitor<T> setParallelCount(int parallelCount) ;
	
	T triggerActions() throws Exception;

	T execute() throws Exception;

	String[] getLogFilterKeys(T t);

}
