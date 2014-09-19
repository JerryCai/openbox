package com.googlecode.openbox.webui;

public class WebTestException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4559473463870760121L;
	public static final String E_FROM = "error from webUI test";

	public WebTestException() {
		super(E_FROM);
	}

	public WebTestException(String message) {
		super(E_FROM + message);
	}

	public WebTestException(String message, Throwable e) {
		super(E_FROM + message, e);
	}

	public WebTestException(Throwable e) {
		super(E_FROM, e);
	}

	public static WebTestException create() {
		return new WebTestException();
	}

	public static WebTestException create(String message) {
		return new WebTestException(message);
	}

	public static WebTestException create(String message, Throwable e) {
		return new WebTestException(message, e);
	}

	public static WebTestException create(Throwable e) {
		return new WebTestException(e);
	}
}
