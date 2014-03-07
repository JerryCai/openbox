package com.google.test.openbox.http.monitors;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.google.test.openbox.http.ExecutorMonitor;

public class CustomCyclicBarrierMonitor implements ExecutorMonitor {

	public static final String NAME = "monitor.CyclicBarrierMonitor";

	private CyclicBarrier startCyclicBarrier;
	private CyclicBarrier endCyclicBarrier;

	private CustomCyclicBarrierMonitor(CyclicBarrier beforeCyclicBarrier,
			CyclicBarrier afterCyclicBarrier) {
		this.startCyclicBarrier = beforeCyclicBarrier;
		this.endCyclicBarrier = afterCyclicBarrier;

	}

	public static CustomCyclicBarrierMonitor create(
			CyclicBarrier beforeCyclicBarrier, CyclicBarrier afterCyclicBarrier) {
		return new CustomCyclicBarrierMonitor(beforeCyclicBarrier, afterCyclicBarrier);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void start() {
		awaitCyclicBarrier(startCyclicBarrier);

	}

	@Override
	public void end() {
		awaitCyclicBarrier(endCyclicBarrier);

	}

	private void awaitCyclicBarrier(CyclicBarrier cyclicBarrier) {
		if (null != cyclicBarrier) {
			try {
				cyclicBarrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}
}
