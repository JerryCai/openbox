package com.google.test.openbox.cors;



import com.google.test.openbox.cors.request.CorsRequest;
import com.google.test.openbox.http.Request;
import com.google.test.openbox.http.RequestProxy;

public class CORSClientProxy implements RequestProxy {

	
	private String origin;

	public CORSClientProxy(String originValue) {
		this.origin = originValue;
	}

	public static CORSClientProxy newInstance(String originValue) {
		return new CORSClientProxy(originValue);
	}

	@Override
	public void executeProxy(Request request) {
		request.addHeader(CorsRequest.HEADER_REQUEST_ORIGIN, origin);
	}


	public String getOrigin() {
		return origin;
	}

}
