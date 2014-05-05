package com.googlecode.openbox.foo;

public class FooClientException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1589331171419683494L;

	public FooClientException() {
		super();
	}

	public FooClientException(String msg) {
		super(msg);
	}

	public FooClientException(String msg, Throwable t) {
		super(msg, t);
	}

	public FooClientException(Throwable t) {
		super(t);
	}

	public static FooClientException create(
			String msg, Throwable e) {
		return new FooClientException("MeetingAPI-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR : " + msg, e);
	}

	public static FooClientException create(
			String msg) {
		return new FooClientException("MeetingAPI-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR : " + msg);
	}

	public static FooClientException create(
			Throwable e) {
		return new FooClientException("MeetingAPI-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR", e);
	}

}
