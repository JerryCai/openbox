package com.googlecode.openbox.server.log.monitor;

public interface ServerLogHandler<T> {

	void action(T t, String logs);

}
