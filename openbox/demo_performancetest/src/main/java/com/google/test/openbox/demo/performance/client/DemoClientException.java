package com.google.test.openbox.demo.performance.client;

public class DemoClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5380952228590875059L;

	public DemoClientException() {
		super();
	}

	public DemoClientException(String msg) {
		super(msg);
	}

	public DemoClientException(String msg, Throwable t) {
		super(msg, t);
	}

	public DemoClientException(Throwable t) {
		super(t);
	}

	public static DemoClientException create(
			String msg, Throwable e) {
		return new DemoClientException("DemoAPI-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR : " + msg, e);
	}

	public static DemoClientException create(
			String msg) {
		return new DemoClientException("DemoAPI-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR : " + msg);
	}

	public static DemoClientException create(
			Throwable e) {
		return new DemoClientException("DemoAPI-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR", e);
	}

}
