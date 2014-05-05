package com.googlecode.openbox.demo.performance;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.demo.performance.requests.DemoProxyRequestParam;
import com.googlecode.openbox.http.TimeLine;
import com.googlecode.openbox.http.monitors.CyclicBarrierMonitor;

public abstract class AbstractRequestSender implements RequestSender {
	private static final Logger logger = LogManager.getLogger();

	private int threadCount;
	private String url;
	private String dcName;
	private CyclicBarrierMonitor cyclicBarrierMonitor;
	private PerformanceDataGroup group;
	private CloseableHttpClient httpClient;

	public AbstractRequestSender(CloseableHttpClient httpClient,
			int threadCount, String url, String dcName) {
		this.httpClient = httpClient;
		this.threadCount = threadCount;
		this.url = url;
		this.dcName = dcName;
		this.cyclicBarrierMonitor = CyclicBarrierMonitor.create(threadCount);
		this.group = PerformanceDataGroup.create("proxy test ", threadCount);
	}

	public final class ClientServerDuration {
		private TimeLine clientTimeLine;
		private long serverDuration;

		public ClientServerDuration(TimeLine clientTimeLine, long serverDuration) {
			this.clientTimeLine = clientTimeLine;
			this.serverDuration = serverDuration;
		}

		public TimeLine getClientTimeLine() {
			return clientTimeLine;
		}

		public long getServerDuration() {
			return serverDuration;
		}

	}

	public abstract ClientServerDuration sendRequest(DemoProxyRequestParam param);

	public final class Task implements Callable<PerformanceData> {
		private DemoProxyRequestParam param;

		public Task() {
			this.param = RequestParamFactory.createDemoProxyRequestParam(500);
		}

		@Override
		public PerformanceData call() throws Exception {
			try {
				param.setId(generateId());
				ClientServerDuration csDuration = sendRequest(param);
				PerformanceData pd = new PerformanceData();
				pd.setId(param.getId());
				pd.setClientTimeLine(csDuration.getClientTimeLine());
				pd.setServerDuration(csDuration.getServerDuration());
				return pd;
			} catch (Exception e) {
				logger.error("execute thread error !!", e);
				throw e;
			}
		}
	}

	@Override
	public void send() {

		ExecutorService es = Executors.newFixedThreadPool(threadCount);
		try {
			@SuppressWarnings("unchecked")
			Future<PerformanceData>[] results = new Future[threadCount];
			for (int i = 0; i < threadCount; i++) {
				results[i] = es.submit(new Task());
			}
			for (int j = 0; j < threadCount; j++) {
				try {
					PerformanceData pd = results[j].get();
					group.addPerformanceData(pd);
				} catch (Exception e) {
					logger.error("error for Future get result ", e);
					throw new RuntimeException(e);
				}

			}
			group.setGroupTimeLine(getCyclicBarrierMonitor().getTimeLine());
		} finally {
			es.shutdown();
			try {
				es.awaitTermination(30, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				logger.error("error for awaitTermination", e);
			}
		}

	}

	@Override
	public PerformanceDataGroup getPerformanceDataGroup() {
		return this.group;
	}

	private String generateId() {
		return "request[" + threadCount + "-T-"
				+ Thread.currentThread().getId() + "]";
	}

	public int getThreadCount() {
		return threadCount;
	}

	public String getUrl() {
		return url;
	}

	public String getDcName() {
		return dcName;
	}

	public CyclicBarrierMonitor getCyclicBarrierMonitor() {
		return cyclicBarrierMonitor;
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

}
