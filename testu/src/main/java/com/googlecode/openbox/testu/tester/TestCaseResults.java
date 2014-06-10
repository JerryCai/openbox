package com.googlecode.openbox.testu.tester;


public class TestCaseResults {

	public static final TestCaseResults DEFAULT_SUCCESS = newInstance(true);

	private TestCaseResults(boolean success) {
		this.success = success;
	}

	public static TestCaseResults newInstance(boolean success) {
		return new TestCaseResults(success);
	}

	private ActualResults actualResults;
	private boolean success;

	public ActualResults getActualResults() {
		return actualResults;
	}

	public void setActualResults(ActualResults actualResults) {
		this.actualResults = actualResults;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
