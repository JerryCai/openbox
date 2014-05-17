package com.googlecode.openbox.testu.concurrent;

import java.util.concurrent.Future;

import com.googlecode.openbox.common.algorithm.Homogenizer.Action;
import com.googlecode.openbox.common.context.CommonContext;
import com.googlecode.openbox.http.Response;
import com.googlecode.openbox.testu.TestResult;

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
