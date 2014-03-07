package com.google.test.openbox.http.monitors;

import com.google.test.openbox.http.ExecutorMonitor;
import com.google.test.openbox.http.TimeLine;

public class TimeLineMonitor implements ExecutorMonitor {
	public static final String NAME = "monitor.TimeRecorder";

	private long startTime;
	private long endTime;

	private TimeLineMonitor() {
		this.startTime = -1;
		this.endTime = -1;
	}

	public static TimeLineMonitor create() {
		return new TimeLineMonitor();
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void start() {
		startTime = System.currentTimeMillis();
	}

	@Override
	public void end() {
		endTime = System.currentTimeMillis();
	}

	public TimeLine toTimeLine() {
		return TimeLine.create(startTime, endTime);
	}
}
