package com.google.test.openbox.cors.request;

import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.util.Args;

import com.google.test.openbox.http.Request;

public class CorsRequest extends Request {
	
	public static final String HEADER_REQUEST_ORIGIN = "Origin";
	public static final String HEADER_REQUEST_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
	public static final String HEADER_REQUEST_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

	public static final String RESPONSE_HEADER_NAME_CORS_CRED = "Access-Control-Allow-Credentials";
	public static final String RESPONSE_HEADER_NAME_CORS_ORIGIN = "Access-Control-Allow-Origin";

	public CorsRequest(String url , String origin,
			String accessControlRequestMethod,
			String accessControlRequestHeaders) {
		super(url);
		addHeader(HEADER_REQUEST_ORIGIN, origin);
		addHeader(HEADER_REQUEST_ACCESS_CONTROL_REQUEST_METHOD,
				accessControlRequestMethod);
		addHeader(HEADER_REQUEST_ACCESS_CONTROL_REQUEST_HEADERS,
				accessControlRequestHeaders);
	}

	@Override
	public HttpEntity getEntity() {
		return null;
	}

	@Override
	public String getMethod() {
		return HttpOptions.METHOD_NAME;
	}

    public Set<String> getAllowedMethods(final HttpResponse response) {
        Args.notNull(response, "HTTP response");

        final HeaderIterator it = response.headerIterator("Allow");
        final Set<String> methods = new HashSet<String>();
        while (it.hasNext()) {
            final Header header = it.nextHeader();
            final HeaderElement[] elements = header.getElements();
            for (final HeaderElement element : elements) {
                methods.add(element.getName());
            }
        }
        return methods;
    }

}
