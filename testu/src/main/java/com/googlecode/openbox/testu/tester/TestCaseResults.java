package com.googlecode.openbox.testu.tester;

import com.google.gson.annotations.Expose;
import com.googlecode.openbox.testu.TestUException;

public class TestCaseResults {

	public enum Result {
		SUCCESS(1), FAILURE(2), SKIP(3),SUCCESS_PERCENTAGE_FAILURE(4),STARTED(16);
		int status;
		private Result(int status){
			this.status = status;
		}
		
		public static Result valueOf(int status){
			Result[] results = Result.values();
			for( Result result : results){
				if(result.status == status){
					return result;
				}
			}
			throw new TestUException("The test status ["+status+"] isn't defined !");
		}
	}

	private TestCaseResults() {
		this.result = Result.SKIP;
	}

	public static TestCaseResults newInstance() {
		return new TestCaseResults();
	}

	@Expose(serialize = false, deserialize = false)
	private ActualResults testActualResults;
	@Expose
	private String[] actualResults;
	@Expose
	private Result result;
	private String duration;
	private String msg;

	public ActualResults getActualResults() {
		return testActualResults;
	}

	public void setActualResults(ActualResults actualResults) {
		this.testActualResults = actualResults;
		if (null != actualResults) {
			this.actualResults = actualResults.value();
		}
	}
	
	

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
