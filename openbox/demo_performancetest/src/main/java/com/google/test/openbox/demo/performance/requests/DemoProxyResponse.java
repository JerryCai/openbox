package com.google.test.openbox.demo.performance.requests;

public class DemoProxyResponse extends DemoProxyRequestParam {

	private String result;
	private String duration;
	private String echo;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getEcho() {
		return echo;
	}

	public void setEcho(String echo) {
		this.echo = echo;
	}

}
