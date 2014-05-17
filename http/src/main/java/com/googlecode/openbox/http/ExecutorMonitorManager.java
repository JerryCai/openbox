package com.googlecode.openbox.http;

import java.util.List;

public interface ExecutorMonitorManager {

	void register(ExecutorMonitor monitor);
	
	void register(List<ExecutorMonitor> subMonitors);

	void unregister(ExecutorMonitor monitor);
	
	void unregister(List<ExecutorMonitor> subMonitors);

	List<ExecutorMonitor> list();
	
	ExecutorMonitor getMonitor(String name);
	
	void unregisterAll();
	
	void startMonitors();

	void endMonitors();
}
