package com.google.test.openbox.http.monitors;

import java.util.concurrent.CountDownLatch;

import com.google.test.openbox.http.ExecutorMonitor;

public class CountDownLatchMonitor implements ExecutorMonitor {
	public static final String NAME = "monitor.CountDownLatchMonitor";

	private CountDownLatch startCountDownLatch;
	private CountDownLatch endCountDownLatch;

	private CountDownLatchMonitor(CountDownLatch startCountDownLatch,
			CountDownLatch endCountDownLatch) {
		this.startCountDownLatch = startCountDownLatch;
		this.endCountDownLatch = endCountDownLatch;
	}

	public static CountDownLatchMonitor create(
			CountDownLatch beforeCountDownLatch,
			CountDownLatch afterCountDownLatch) {
		return new CountDownLatchMonitor(beforeCountDownLatch,
				afterCountDownLatch);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void start() {
		waitCountDownLatch(startCountDownLatch);
	}

	@Override
	public void end() {
		waitCountDownLatch(endCountDownLatch);
	}

	private void waitCountDownLatch(CountDownLatch countDownLatch) {
		if (null != countDownLatch) {
			countDownLatch.countDown();
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
