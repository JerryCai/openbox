package com.googlecode.openbox.testu.concurrent;

import java.util.concurrent.Callable;

import com.googlecode.openbox.common.context.CommonContext;
import com.googlecode.openbox.http.Response;
import com.googlecode.openbox.http.monitors.CyclicBarrierMonitor;

public abstract class ConcurrentStep implements Callable<Response> {
	private int index;
	private CommonContext caseContext;
	private CyclicBarrierMonitor cyclicBarrierMonitor;

	public ConcurrentStep(CommonContext caseContext, int index) {
		this.caseContext = caseContext;
		this.index = index;
	}

	public String generateIndexId(String key) {
		return key + "-" + index;
	}

	@Override
	public Response call() throws Exception {
		Response response = action(caseContext,cyclicBarrierMonitor);
		return response;
	}

	public abstract Response action(CommonContext caseContext , CyclicBarrierMonitor cyclicBarrierMonitor);

	public CyclicBarrierMonitor getCyclicBarrierMonitor() {
		return cyclicBarrierMonitor;
	}

	public void setCyclicBarrierMonitor(
			CyclicBarrierMonitor cyclicBarrierMonitor) {
		this.cyclicBarrierMonitor = cyclicBarrierMonitor;
	}

	public int getIndex() {
		return index;
	}

}
