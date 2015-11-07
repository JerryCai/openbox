package com.googlecode.openbox.server.log.monitor;

import java.util.List;

import com.googlecode.openbox.server.log.ServerLog;

public interface ServerLogProvider {
	
	List<ServerLog> getServerLogs();
}
