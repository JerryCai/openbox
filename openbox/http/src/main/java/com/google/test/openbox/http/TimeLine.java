package com.google.test.openbox.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TimeLine {
	private static final Logger logger = LogManager.getLogger();

	private long startTime;
	private long endTime;
	private long duration;

	private TimeLine(long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = this.endTime - this.startTime;
	}

	public static TimeLine create(long startTime, long endTime) {
		return new TimeLine(startTime, endTime);
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getDuration() {
		return duration;
	}

	public String toString() {
		return "startTime=[" + HttpDateUtils.getDateString(startTime) + "],endTime=[" + HttpDateUtils.getDateString(endTime)
				+ "] ==> duration=[" + duration + "]ms";
	}

	public void printLog() {
		if (logger.isInfoEnabled()) {
			logger.info("\n[" + Thread.currentThread().getName()+ "-T-"
					+ Thread.currentThread().getId() + "]==>" + toString());
		}
	}
}
