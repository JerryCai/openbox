package com.googlecode.openbox.demo.performance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.demo.performance.report.JFreeChartPerformanceReport;
import com.googlecode.openbox.demo.performance.report.TextPerformanceReport;

public class RunManager {
	private static final Logger logger = LogManager.getLogger();

	private String url;
	private String dcName;
	private int[] groups;

	private List<PerformanceReport> reports;

	private RunManager(String url, String dcName, int[] groups) {
		this.url = url;
		this.dcName = dcName;
		this.groups = groups;
		this.reports = new ArrayList<PerformanceReport>(3);

	}

	public void addReporter(PerformanceReport reporter) {
		this.reports.add(reporter);
	}

	public void unRegisterPerformanceReport(PerformanceReport reporter) {
		this.reports.remove(reporter);
	}

	public static RunManager create(String url, String dcName, int[] groups) {
		return new RunManager(url, dcName, groups);
	}

	public static int[] generateGroups(int from, int to, int step) {
		from = from - from % step;
		int num = (to - from) / step + 1;

		int[] groups = new int[num];
		int value = from;
		int i = 0;
		while (value <= to) {
			if (value <= 0) {
				groups[i] = 1;
			} else {
				groups[i] = value;
			}
			value = value + step;
			i++;

		}
		;
		if (logger.isInfoEnabled()) {
			logger.info("gernerated thread groups : {}", groups);
		}
		return groups;
	}

	private PerformanceDataGroup sendEachGroupRequest(int threadCount) {
		int routeNum = 1;
		int size = threadCount + 1;
		int perRouteSize = size / routeNum;
		CloseableHttpClient httpClient = HttpClients.custom()
				.setMaxConnTotal(perRouteSize * routeNum)
				.setMaxConnPerRoute(perRouteSize)
				.setRetryHandler(new HttpRequestRetryHandler() {
					@Override
					public boolean retryRequest(IOException exception,
							int executionCount, HttpContext context) {
						if (executionCount > 3) {
							logger.error("Maximum tries reached for client http pool ");
							return false;
						}
						if (exception instanceof NoHttpResponseException) {
							logger.error("No response from server on "
									+ executionCount + " call");
							return true;
						}
						return false;
					}
				}).build();
		try {
			// warmup
			new ClientModeRequestSender(httpClient, 1, url, dcName).send();
			RequestSender requestSender = new ClientModeRequestSender(
					httpClient, threadCount, url, dcName);
			requestSender.send();
			return requestSender.getPerformanceDataGroup();
		} finally {
			HttpClientUtils.closeQuietly(httpClient);
		}
	}

	public void start() {

		int groupsCount = groups.length;
		PerformanceDataGroup[] performanceDataGroups = new PerformanceDataGroup[groupsCount];
		for (int i = 0; i < groupsCount; i++) {
			performanceDataGroups[i] = sendEachGroupRequest(groups[i]);
		}

		for (PerformanceReport reporter : reports) {
			reporter.report(performanceDataGroups);
		}
	}

	public static void main(String... args) {
		String url = "http://111.11.11.11:8999";
		String dcName = "dc2";
		int[] groups = generateGroups(1, 500, 10);
		RunManager runManager = RunManager.create(url, dcName, groups);
		runManager.addReporter(TextPerformanceReport.create(null, null));
		runManager.addReporter(JFreeChartPerformanceReport.create(
				"Performance Report", null));

		runManager.start();
	}

}
