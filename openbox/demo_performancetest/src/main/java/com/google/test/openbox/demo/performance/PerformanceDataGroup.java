package com.google.test.openbox.demo.performance;

import com.google.test.openbox.common.UtilsAPI;
import com.google.test.openbox.http.GsonFactory;
import com.google.test.openbox.http.TimeLine;

public class PerformanceDataGroup {

	private String name;
	private int totalCount;
	private TimeLine groupTimeLine;
	private PerformanceData[] performanceDatas;
	private int index;

	private PerformanceDataGroup(String name, int totalCount) {
		this.name = name;
		this.totalCount = totalCount;
		this.performanceDatas = new PerformanceData[totalCount];
		this.index = 0;
	}

	public static PerformanceDataGroup create(String name, int totalCount) {
		return new PerformanceDataGroup(name, totalCount);
	}

	public synchronized void addPerformanceData(PerformanceData pd) {
		performanceDatas[index++] = pd;
	}

	public TimeLine getGroupTimeLine() {
		return groupTimeLine;
	}

	public void setGroupTimeLine(TimeLine groupTimeLine) {
		this.groupTimeLine = groupTimeLine;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getIndex() {
		return index;
	}

	public long getClientAverageDuration() {

		long totalDuration = 0L;
		for (int i = 0; i < performanceDatas.length; i++) {
			PerformanceData performanceData = performanceDatas[i];
			totalDuration = totalDuration
					+ performanceData.getClientTimeLine().getDuration();
		}

		return totalDuration / performanceDatas.length;
	}

	public long getServerAverageDuration() {

		long totalDuration = 0L;
		for (int i = 0; i < performanceDatas.length; i++) {
			PerformanceData performanceData = performanceDatas[i];
			totalDuration = totalDuration + performanceData.getServerDuration();
		}

		return totalDuration / performanceDatas.length;
	}

	public static final int OVERALL_WIDTH = PerformanceData.FIX_WIDTH
			* PerformanceData.COLOUMN_NUM + PerformanceData.COLOUMN_NUM - 1;

	public String getPerformanceInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("|")
				.append(UtilsAPI.getFixedColumn(OVERALL_WIDTH, "-", name))
				.append("\n");
		String overInfo = "totalCount=[" + totalCount + "], totalDuration=["
				+ groupTimeLine.getDuration() + "]ms, serverAverageDuration=["
				+ getServerAverageDuration()
				+ "]ms/request, clientAverageDuration=["
				+ getClientAverageDuration() + "]ms/request";
		sb.append("|").append(
				UtilsAPI.getFixedColumn(OVERALL_WIDTH, " ", overInfo));
		sb.append("\n");
		sb.append("|").append(UtilsAPI.getFixedColumn(OVERALL_WIDTH, "-", ""));
		sb.append("\n");
		for (int i = 0; i < performanceDatas.length; i++) {
			PerformanceData pd = performanceDatas[i];
			if (i == 0) {
				sb.append(pd.getHeaderExpress());
				sb.append("\n");
			}
			sb.append(pd);
			sb.append("\n");
		}

		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public PerformanceData[] getPerformanceDatas() {
		return performanceDatas;
	}

	public String toString() {
		return GsonFactory.createGson().toJson(this);
	}

}
