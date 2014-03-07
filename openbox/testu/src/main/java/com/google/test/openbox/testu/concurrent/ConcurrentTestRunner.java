package com.google.test.openbox.testu.concurrent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.test.openbox.common.algorithm.Homogenizer;
import com.google.test.openbox.common.algorithm.Homogenizer.Action;
import com.google.test.openbox.common.context.BasicContext;
import com.google.test.openbox.common.context.CommonContext;
import com.google.test.openbox.http.Response;
import com.google.test.openbox.http.monitors.CyclicBarrierMonitor;
import com.google.test.openbox.testu.TestResult;
import com.google.test.openbox.testu.TestUException;

public abstract class ConcurrentTestRunner {
	private String runName;

	public ConcurrentTestRunner(String runName) {
		this.runName = runName;

	}

	public abstract void precondition(final CommonContext caseContext);

	public abstract StepGroup[] getConcurrentStep(
			final CommonContext caseContext);

	public abstract ValidatorGroup[] getStepValidator(
			CommonContext caseContext, Future<Response>[] results);

	public abstract void validate(final CommonContext caseContext)
			throws Exception;

	public void start() throws Exception {

		CommonContext caseContext = new BasicContext();
		precondition(caseContext);
		Action<ConcurrentStep>[] actions = getConcurrentStep(caseContext);
		List<ConcurrentStep> tasks = Homogenizer.homogenize(actions);
		if (null == tasks) {
			throw TestUException
					.create("concurrent test run "
							+ runName
							+ " error as getConcurrentStep is null, Please check related test case implentation ");
		}
		int threadNum = tasks.size();

		if (threadNum <= 1) {
			throw TestUException
					.create("concurrent test "
							+ runName
							+ " case implentation invalidated as getConcurrentStep just return  threadNum<=1");
		}

		ExecutorService executorService = Executors
				.newFixedThreadPool(threadNum);

		CyclicBarrierMonitor cyclicBarrierMonitor = CyclicBarrierMonitor
				.create(threadNum);

		@SuppressWarnings("unchecked")
		Future<Response>[] results = new Future[threadNum];

		for (int i = 0; i < threadNum; i++) {
			ConcurrentStep task = tasks.get(i);
			task.setCyclicBarrierMonitor(cyclicBarrierMonitor);
			Future<Response> result = executorService.submit(task);
			results[i] = result;
		}
		ValidatorGroup[] stepValidators = getStepValidator(caseContext, results);
		List<TestResult> testResults = Homogenizer.homogenize(stepValidators);
		TestResult.check(testResults);
		validate(caseContext);
		executorService.shutdown();
		try {
			executorService.awaitTermination(10, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
