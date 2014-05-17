package com.googlecode.openbox.cors;



import com.googlecode.openbox.cors.request.CorsRequest;
import com.googlecode.openbox.http.Request;
import com.googlecode.openbox.http.RequestProxy;

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
