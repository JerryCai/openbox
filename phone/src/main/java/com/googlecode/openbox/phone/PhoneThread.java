package com.googlecode.openbox.phone;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class PhoneThread implements Runnable {
	private static final Logger logger = LogManager.getLogger();

	boolean executeSuccess;
	public static final int TIMEOUT_SECONDS = 15;
	private CyclicBarrier barrier;
	private int operationInterval;

	public PhoneThread(int operationInterval) {
		this.operationInterval = operationInterval;
		this.executeSuccess = false;
		this.barrier = new CyclicBarrier(2);
	}

	@Override
	public void run() {
		try {
			action();
			TimeUnit.SECONDS.sleep(operationInterval);
			executeSuccess = true;
		} catch (Exception e) {
			logger.error(e);
			throw new PhoneException(e);
		} finally {
			try {
				waitDone();
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	public abstract void action();

	public boolean isExecuteSuccess() {
		return executeSuccess;
	}

	private void waitDone() throws InterruptedException,
			BrokenBarrierException, TimeoutException {
		barrier.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
	}

	public static boolean execute(PhoneThread phoneThread) {
		Thread t = new Thread(phoneThread);
		t.start();
		try {
			phoneThread.waitDone();
		} catch (Exception e) {
			logger.error(
					"Timeout when wait phone task done, ignore this error and continue !",
					e);
			// throw new PhoneException(e);
		}
		return phoneThread.isExecuteSuccess();
	}

}
