package com.google.test.openbox.http.monitors;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.test.openbox.http.ExecutorMonitor;
import com.google.test.openbox.http.TimeLine;

public class CyclicBarrierMonitor implements ExecutorMonitor {
	private static final Logger logger = LogManager.getLogger();

	public static final String NAME = "monitor.CyclicBarrierMonitor";

	private CyclicBarrier startCyclicBarrier;
	private CyclicBarrier endCyclicBarrier;
	private TimeLineMonitor timeLineMonitor;
	private int threadNum;

	private CyclicBarrierMonitor(final int threadNum) {
		this.threadNum = threadNum;
		this.timeLineMonitor = TimeLineMonitor.create();
		this.startCyclicBarrier = new CyclicBarrier(this.threadNum,
				new Runnable() {

					@Override
					public void run() {
						timeLineMonitor.start();

						if (logger.isInfoEnabled()) {
							logger.info("\n#########################CyclicBarrierMonitor-> totally have ["
									+ threadNum
									+ "] concurrent requests are arrived at CyclicBarrier and begin to send them out now############################ ");
						}
					}
				});
		this.endCyclicBarrier = new CyclicBarrier(this.threadNum,
				new Runnable() {

					@Override
					public void run() {
						timeLineMonitor.end();

						if (logger.isInfoEnabled()) {
							logger.info("\n#########################CyclicBarrierMonitor-> complete and receive totally ["
									+ threadNum
									+ "] concurrent requests' resposne now and go on main thread ############################ ");
						}
					}
				});

	}

	public static CyclicBarrierMonitor create(int threadNum) {
		return new CyclicBarrierMonitor(threadNum);
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

	public TimeLine getTimeLine() {
		return timeLineMonitor.toTimeLine();
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

	public int getThreadNum() {
		return threadNum;
	}

	public CyclicBarrier getStartCyclicBarrier() {
		return startCyclicBarrier;
	}

	public void setStartCyclicBarrier(CyclicBarrier startCyclicBarrier) {
		this.startCyclicBarrier = startCyclicBarrier;
	}

	public CyclicBarrier getEndCyclicBarrier() {
		return endCyclicBarrier;
	}

	public void setEndCyclicBarrier(CyclicBarrier endCyclicBarrier) {
		this.endCyclicBarrier = endCyclicBarrier;
	}
}
