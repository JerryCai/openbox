package com.googlecode.openbox.server.log.monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogPrintHandler<T> implements ServerLogHandler<T> {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void action(T t, String logs) {
		if (logger.isInfoEnabled()) {
			logger.info("###################################################");
			logger.info(logs);
			logger.info("###################################################");
		}
	}

}
