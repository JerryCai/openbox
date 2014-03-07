package com.google.test.openbox.testu.concurrent;

import java.util.concurrent.Future;

import com.google.test.openbox.common.algorithm.Homogenizer.Action;
import com.google.test.openbox.common.context.CommonContext;
import com.google.test.openbox.http.Response;
import com.google.test.openbox.testu.TestResult;

public abstract class ValidatorGroup implements Action<TestResult> {

	private String name;
	private int num;
	private CommonContext caseContext;
	private Future<Response>[] results;

	public ValidatorGroup(String name, int num, CommonContext caseContext ,Future<Response>[] results) {
		this.name = name;
		this.num = num;
		this.caseContext = caseContext;
		this.results = results;

	}

	public abstract TestResult checkResult(CommonContext caseContext ,Future<Response> result, int counter);

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getNum() {
		return num;
	}

	@Override
	public TestResult handle(int index, int counter) {
		return checkResult(caseContext ,results[index], counter);
	}

}
