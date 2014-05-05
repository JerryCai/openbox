package com.googlecode.openbox.testu;

public class TestUException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestUException() {
		super();
	}

	public TestUException(String msg) {
		super(msg);
	}

	public TestUException(String msg, Throwable t) {
		super(msg, t);
	}

	public TestUException(Throwable t) {
		super(t);
	}

	public static TestUException create(String msg, Throwable e) {
		return new TestUException("TestU-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR : " + msg, e);
	}

	public static TestUException create(String msg) {
		return new TestUException("TestU-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR : " + msg);
	}

	public static TestUException create(Throwable e) {
		return new TestUException("TestU-["
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "]-ERROR", e);
	}

}
