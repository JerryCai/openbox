package com.googlecode.openbox.testu;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class TestResult {
	private static final Logger logger = LogManager.getLogger();
	
	private StringBuilder sb;
	private boolean result;
	private int index;

	private TestResult() {
		this.result = true;
		this.index = 0;
		this.sb = new StringBuilder();
	}

	public static TestResult create() {
		return new TestResult();
	}

	public boolean isSuccess() {
		return result;
	}

	private void setOverallResult(boolean result) {
		this.result = result;
	}

	public void setRuntimeError(String message, Throwable t) {

		setOverallResult(false);
		sb.append("runtime error [").append(message).append("] as ")
				.append(t.getMessage()).append("\n");
	}

	public void setFail(String message) {
		setOverallResult(false);
		sb.append("failed as  [").append(message).append("]\n");
	}

	public void addMessage(String message) {
		sb.append(message);
	}

	public void setItem(String fieldName, Object actual, Object expected,
			boolean result) {
		if (!result) {
			setOverallResult(result);
		}

		sb.append("checkpoints[").append(++index).append("].[").append(result)
				.append("]-->[").append(fieldName).append("]-->")
				.append(" as actual=[").append(actual).append("] , expected=[")
				.append(expected).append("]\n");

	}

	public int getCheckpointNum() {
		return index;
	}

	public void setResult(String message, boolean result) {
		if (!result) {
			setOverallResult(result);
		}
		sb.append(message);
	}

	public String getText() {
		return sb.toString();
	}

	public void merge(TestResult subResult) {
		this.setResult(subResult.getText(), subResult.isSuccess());
	}
	
	public void check(){
		check(this);
	}
	
	public static void check(TestResult testResult){
		if (logger.isInfoEnabled()){
			logger.info(testResult.getText());
		}
		Assert.assertTrue(testResult.isSuccess(), testResult.getText());
	}
	
	public static void check(List<TestResult> testResults){
		TestResult testResult = TestResult.create();
		for(TestResult result : testResults){
			testResult.merge(result);
		}
		check(testResult);
	}
	
	public static void check(TestResult[] testResults){
		TestResult testResult = TestResult.create();
		for(TestResult result : testResults){
			testResult.merge(result);
		}
		check(testResult);
	}
	
}
