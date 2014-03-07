package com.google.test.openbox.server;

public interface ServerValidator {

	public interface CommandGenerator {
		String generate(String seed);
	}

	public interface Checker {
		boolean check(String excepted, String actual);
	}

	boolean checkEnvironmentVariableEqual(String varName, String varValue);

	boolean checkEnvironmentVariableInclude(String varName, String varValue);

	boolean checkEnvironmentVariable(String varName, String varValue,
			Checker checker);

	boolean checkSysctlVariable(String varName, String varValue);

	boolean checkUlimitVariable(String varName, String varValue);

	boolean executeCheck(Checker checker, String command, String... expecteds);

	boolean executeCheckInclude(String command, String... expecteds);

	boolean executeCheckEqual(String command, String... expecteds);

	boolean executeCheckInclude(CommandGenerator cmdGenerator, String seed,
			String... expecteds);

	boolean executeCheckEqual(CommandGenerator cmdGenerator, String seed,
			String... expecteds);

	boolean executeCheck(CommandGenerator cmdGenerator, Checker checker,
			String seed, String... expecteds);

	public static final CommandGenerator DEFAULT_COMMAND_GENERATOR = new CommandGenerator() {
		@Override
		public String generate(String seed) {
			return seed;
		}
	};
	public static final CommandGenerator ENV_VAR_COMMAND_GENERATOR = new CommandGenerator() {
		@Override
		public String generate(String seed) {
			return "echo ${" + seed + "}";
		}
	};
	public static final CommandGenerator SYSCTL_COMMAND_GENERATOR = new CommandGenerator() {
		@Override
		public String generate(String seed) {
			return "sysctl -a | grep " + seed;
		}
	};
	public static final CommandGenerator ULIMIT_COMMAND_GENERATOR = new CommandGenerator() {
		@Override
		public String generate(String seed) {
			return "ulimit -a | grep " + seed;
		}
	};
	public static final Checker EQ_CHECKER = new Checker() {

		@Override
		public boolean check(String excepted, String actual) {
			if (null == excepted || null == actual) {
				return false;
			}
			return excepted.endsWith(actual);
		}
	};
	public static final Checker INCLUDE_CHECKER = new Checker() {

		@Override
		public boolean check(String excepted, String actual) {
			if (null == excepted || null == actual) {
				return false;
			}
			return actual.indexOf(excepted) > -1;
		}
	};

}