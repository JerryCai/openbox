package com.google.test.openbox.http.httpbuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.test.openbox.http.monitors.CyclicBarrierMonitor;

public class MultiThreadDemo {

	public static class Task implements Runnable {
		private CyclicBarrierMonitor cyclicBarrier;

		private int i;

		public Task(CyclicBarrierMonitor cyclicBarrier, int i) {
			this.cyclicBarrier = cyclicBarrier;
			this.i = i;
		}

		@Override
		public void run() {
			HttpBuilder httpBuilder = HttpBuilder.create().setMethod("GET")
					.setScheme("http").setHost("10.224.64.30").setPort(8999)
					.addParameter("number", "" + i);
			httpBuilder.getExecutorMonitorManager().register(cyclicBarrier);
			httpBuilder.execute();
		}

	};

	public static void main(String... args) {

		int nThreads = 200;
		CyclicBarrierMonitor cyclicBarrier = CyclicBarrierMonitor
				.create(nThreads);
		ExecutorService es = Executors.newFixedThreadPool(nThreads);

		for (int loop = 0; loop < 1000; loop++) {
			for (int i = 0; i < nThreads; i++) {
				es.execute(new Task(cyclicBarrier, i));
			}
		}

		es.shutdown();
		try {
			es.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
