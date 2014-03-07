package org.eclipse.plugin.openbox.apiunit.core.runner.ui;

public class TestResult {
	public static final String[] COLUMN_NAME = { "Case Name",/* "Run Progress",*/
			"Duration", "Test Result", "Message" };

	public static final int UNKNOWN = -1;
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int CASE_ERROR =2;

	private String caseName;
	private int runProgress;
	private long duration;
	private int result;
	private String message;
	private String details;

	public TestResult(String caseName) {
		this.caseName = caseName;
		initHook();
	}

	private void initHook() {
		this.runProgress = 0;
		this.duration = 0;
		this.result = UNKNOWN;
		this.message = caseName + " hasn't executed";
	}

	public void startTest() {
		runProgress = 0;
		duration = System.currentTimeMillis();
	}

	public void endTest() {
		duration = System.currentTimeMillis() - duration;
		runProgress = 100;
	}

	public String getTestResultStringExp() {
		if (SUCCESS == getResult())
			return "success";
		if (FAILURE == getResult())
			return "failure";
		if(CASE_ERROR == getResult())
			return "case error";
		return "unkonwn";
	}
	
	public static String getTestResultStringExp(int testResult){
		if (SUCCESS == testResult)
			return "success";
		if (FAILURE == testResult)
			return "failure";
		if(CASE_ERROR == testResult)
			return "case error";
		return "unkonwn";
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRunProgress() {
		return runProgress;
	}

	public void setRunProgress(int runProgress) {
		this.runProgress = runProgress;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
