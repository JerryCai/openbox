package com.google.test.openbox.cors.request;

import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

import com.google.test.openbox.http.ExecutorMonitorManager;
import com.google.test.openbox.http.responses.CommonResponse;

public class CorsResponse extends CommonResponse {
	private Set<String> allowedMethods;

	public CorsResponse(HttpResponse httpResponse, HttpContext httpContext,
			ExecutorMonitorManager executorMonitorManager) {
		super(httpResponse, httpContext, executorMonitorManager);
		this.allowedMethods = parseAllowedMethods(httpResponse);
	}

	public Set<String> getAllowedMethods() {
		return allowedMethods;
	}

	private Set<String> parseAllowedMethods(final HttpResponse response) {
		Args.notNull(response, "Cors HTTP response is null!");

		if (200 != getStatusCode()) {
			return null;
		}
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
