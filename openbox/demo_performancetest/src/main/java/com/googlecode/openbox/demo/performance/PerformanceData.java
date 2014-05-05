package com.googlecode.openbox.demo.performance;

import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.http.TimeLine;

public class PerformanceData {
	private String id;
	private TimeLine clientTimeLine;
	private long serverDuration;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TimeLine getClientTimeLine() {
		return clientTimeLine;
	}

	public void setClientTimeLine(TimeLine clientTimeLine) {
		this.clientTimeLine = clientTimeLine;
	}

	public long getServerDuration() {
		return serverDuration;
	}

	public long getTransferTime() {
		return clientTimeLine.getDuration() - serverDuration;
	}

	public void setServerDuration(long serverDuration) {
		this.serverDuration = serverDuration;
	}

	public final static int FIX_WIDTH = 30;
	public static final int COLOUMN_NUM=4;
	
	private String getHeaderString(String name) {
		return getFixedColumn("-", name);

	}

	private String getHeaderValue(String value) {
		return getFixedColumn(" ", value);
	}

	private String getFixedColumn(String fullString, String item) {
		return UtilsAPI.getFixedColumn(FIX_WIDTH, fullString, item);
	}

	public String getHeaderExpress() {
		StringBuilder sb = new StringBuilder();
		sb.append("|").append(getHeaderString("id"))
				.append(getHeaderString("serverDuration"))
				.append(getHeaderString("clientDuration"))
				.append(getHeaderString("transferDuration"));
		return sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("|")
				.append(getHeaderValue(id))
				.append(getHeaderValue("" + serverDuration + "ms"))
				.append(getHeaderValue("" + clientTimeLine.getDuration() + "ms"))
				.append(getHeaderValue("" + getTransferTime() + "ms"));
		return sb.toString();
	}

}
