package com.googlecode.openbox.phone;

import net.sourceforge.peers.Logger;

import org.apache.logging.log4j.LogManager;

public class PhoneLogger implements Logger {
	private static final PhoneLogger instance = new PhoneLogger();

	private PhoneLogger() {

	}

	public static PhoneLogger getInstance() {
		return instance;
	}

	@Override
	public void debug(String message) {
		LogManager.getLogger().debug(message);
	}

	@Override
	public void info(String message) {
		LogManager.getLogger().info(message);

	}

	@Override
	public void error(String message) {
		LogManager.getLogger().error(message);

	}

	@Override
	public void error(String message, Exception exception) {
		LogManager.getLogger().error(message, exception);

	}

	@Override
	public void traceNetwork(String message, String direction) {

		LogManager
				.getLogger()
				.debug("\n-------------------------------[sip trace]-------------------------------\n"
						+ direction
						+ "\n\n"
						+ message
						+ "\n-------------------------------------------------------------------------\n");
	}

}
