package com.googlecode.openbox.http;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExecutorMonitorManagerImpl implements ExecutorMonitorManager {
	private static final Logger logger = LogManager.getLogger();
	
	private List<ExecutorMonitor> monitors;

	private ExecutorMonitorManagerImpl() {
		this.monitors = new LinkedList<ExecutorMonitor>();
	}

	public static ExecutorMonitorManagerImpl newInstance() {
		return new ExecutorMonitorManagerImpl();
	}

	@Override
	public void register(ExecutorMonitor monitor) {
		monitors.add(monitor);
	}

	@Override
	public void unregister(ExecutorMonitor monitor) {
		monitors.remove(monitor);
	}

	@Override
	public void register(List<ExecutorMonitor> subMonitors) {
		monitors.addAll(subMonitors);
	}

	@Override
	public void unregister(List<ExecutorMonitor> subMonitors) {
		monitors.removeAll(subMonitors);
	}

	@Override
	public List<ExecutorMonitor> list() {
		return monitors;
	}

	@Override
	public ExecutorMonitor getMonitor(String name) {
		for (ExecutorMonitor monitor : monitors) {
			if (monitor.getName().equals(name)) {
				return monitor;
			}
		}
		return null;
	}

	@Override
	public void unregisterAll() {
		monitors.clear();
	}

	@Override
	public void startMonitors() {
		List<ExecutorMonitor> monitors = list();
		// start will keep add sequence
		for (int index = 0; index < monitors.size(); index++) {
			ExecutorMonitor monitor = monitors.get(index);
			monitor.start();
			if(logger.isDebugEnabled()){
				logger.debug("\n monitor["+index+"]-->"+monitor.getName()+" started ~~~~~~");
			}
		}
	}

	@Override
	public void endMonitors() {
		List<ExecutorMonitor> monitors = list();
		int num = monitors.size();
		// for end , it will keep close pair with start .
		for (int index = num - 1; index >= 0; index--) {
			ExecutorMonitor monitor = monitors.get(index);
			monitor.end();
			if(logger.isDebugEnabled()){
				logger.debug("\n monitor["+index+"]-->"+monitor.getName()+" ended ~~~~~~");
			}
		}
	}

}
