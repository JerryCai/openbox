package com.googlecode.openbox.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerValidatorImpl implements ServerValidator {
	private static final Logger logger = LogManager.getLogger();

	private ServerGroup serverGroup;

	public ServerValidatorImpl(ServerGroup serverGroup) {
		this.serverGroup = serverGroup;
	}

	public static ServerValidatorImpl newInstance(ServerGroup serverGroup) {
		return new ServerValidatorImpl(serverGroup);
	}

	private ServerGroup getServerGroup() {
		return serverGroup;
	}

	private String[] executeCommand(String command) {
		return getServerGroup().executeSingleCommandGetResponse(command);
	}

	@Override
	public boolean executeCheck(Checker checker, String command,
			String... expecteds) {
		return executeCheck(DEFAULT_COMMAND_GENERATOR, checker, command,
				expecteds);
	}

	@Override
	public boolean checkEnvironmentVariableEqual(String varName, String varValue) {
		return checkEnvironmentVariable(varName, varValue, EQ_CHECKER);
	}

	@Override
	public boolean checkEnvironmentVariableInclude(String varName,
			String varValue) {
		return checkEnvironmentVariable(varName, varValue, INCLUDE_CHECKER);
	}

	@Override
	public boolean checkEnvironmentVariable(String varName, String varValue,
			Checker checker) {
		return executeCheck(ENV_VAR_COMMAND_GENERATOR, checker, varName,
				varValue);
	}

	@Override
	public boolean checkSysctlVariable(String varName, String varValue) {
		return executeCheck(SYSCTL_COMMAND_GENERATOR, INCLUDE_CHECKER, varName,
				varValue);
	}

	@Override
	public boolean checkUlimitVariable(String varName, String varValue) {
		return executeCheck(ULIMIT_COMMAND_GENERATOR, INCLUDE_CHECKER, varName,
				varValue);
	}

	@Override
	public boolean executeCheck(CommandGenerator cmdGenerator, Checker checker,
			String seed, String... expecteds) {
		String checkCmd = cmdGenerator.generate(seed);
		String[] values = executeCommand(checkCmd);
		boolean result = true;
		for (String actual : values) {
			for (String expected : expecteds) {
				if (checker.check(expected, actual)) {
					logger.info("[SUCCESS]environment check as [" + seed
							+ "],exceptedValue=[" + expected
							+ "],actualValue=[" + actual + "]");
				} else {
					logger.info("[FAIL]environment check as [" + seed
							+ "],exceptedValue=[" + expected
							+ "],actualValue=[" + actual + "]");
					result = false;
				}
			}
		}
		logger.info("above group overall check result is [" + result + "]");
		return result;
	}

	@Override
	public boolean executeCheckInclude(String command, String... expecteds) {
		return executeCheck(INCLUDE_CHECKER, command, expecteds);
	}

	@Override
	public boolean executeCheckEqual(String command, String... expecteds) {
		return executeCheck(EQ_CHECKER, command, expecteds);
	}

	@Override
	public boolean executeCheckInclude(CommandGenerator cmdGenerator,
			String seed, String... expecteds) {
		return executeCheck(cmdGenerator, INCLUDE_CHECKER, seed, expecteds);
	}

	@Override
	public boolean executeCheckEqual(CommandGenerator cmdGenerator,
			String seed, String... expecteds) {
		return executeCheck(cmdGenerator, EQ_CHECKER, seed, expecteds);
	}

}
