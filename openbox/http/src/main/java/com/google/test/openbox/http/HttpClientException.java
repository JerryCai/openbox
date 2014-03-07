package com.google.test.openbox.http;

public class HttpClientException extends RuntimeException {

	public static final String E_FROM = "error from Http Client part ";
	/**
	 * 
	 */
	private static final long serialVersionUID = -6665353979442329254L;

	public HttpClientException() {
		super(E_FROM);
	}

	public HttpClientException(String message) {
		super(E_FROM + message);
	}

	public HttpClientException(String message, Throwable e) {
		super(E_FROM + message, e);
	}

	public HttpClientException(Throwable e) {
		super(E_FROM, e);
	}

	public static HttpClientException create() {
		return new HttpClientException();
	}

	public static HttpClientException create(String message) {
		return new HttpClientException(message);
	}

	public static HttpClientException create(String message, Throwable e) {
		return new HttpClientException(message, e);
	}

	public static HttpClientException create(Throwable e) {
		return new HttpClientException(e);
	}

}
