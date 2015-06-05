package com.googlecode.openbox.http;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RequestConverter {
	private static final Logger logger = LogManager.getLogger();

	private RequestConverter() {
	}

	public static HttpRequestBase toHttpRequestBase(final RequestBuilder request) {
		HttpRequestBase httpRequestBase = null;
		if (null == request.getEntity()) {
			httpRequestBase = getNoBodyRequestBase(request);
		} else {
			httpRequestBase = getHasBodyRequest(request);
		}

		return internBuild(httpRequestBase, request);

	}

	public static String getHttpRequestLog(HttpRequestBase request,
			HttpEntity requestEntity) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\n===================================== [T-"
					+ Thread.currentThread().getId()
					+ " Request ]===============================================\n");
			sb.append(request.getMethod()).append("\n")
					.append(request.getURI().toString()).append("\n");
			sb.append("------------------------headers--------------------------\n");
			Header[] headers = request.getAllHeaders();
			if (null != headers) {
				for (Header header : headers) {
					sb.append(header.getName()).append(":")
							.append(header.getValue()).append("\n");
				}
			}
			sb.append("------------------------ body --------------------------\n");
			if (null != requestEntity) {
				sb.append(EntityUtils.toString(requestEntity, "UTF-8"));
			}
			sb.append("\n===================================================================================================\n");
			return sb.toString();
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	private static HttpRequestBase getHasBodyRequest(
			final RequestBuilder request) {
		return HttpRequestFactory.createHasBodyRequest(request.getMethod());
	}

	private static HttpRequestBase getNoBodyRequestBase(
			final RequestBuilder request) {
		return HttpRequestFactory.createNoBodyRequestBase(request.getMethod());
	}

	private static HttpRequestBase internBuild(
			final HttpRequestBase httpRequestBase, final RequestBuilder request) {

		buildURI(httpRequestBase, request);
		buildHeaders(httpRequestBase, request);

		if (httpRequestBase instanceof HttpEntityEnclosingRequestBase) {
			buildEntity((HttpEntityEnclosingRequestBase) httpRequestBase,
					request);
		}

		RequestConfig config = request.getRequestConfig();
		if (null != config) {
			httpRequestBase.setConfig(config);
		}
		return httpRequestBase;
	}

	private static void buildURI(final HttpRequestBase httpRequestBase,
			final RequestBuilder request) {
		// build URI with query by URIBuilder content base on client request
		// implementation .
		try {
			httpRequestBase.setURI(request.getURIBuilder().build());
		} catch (URISyntaxException e) {
			String msg = "request URIBuilder do build error !";
			logger.error(msg, e);
			throw new HttpClientException(msg, e);
		}
	}

	private static void buildHeaders(final HttpRequestBase httpRequestBase,
			final RequestBuilder request) {
		// build Headers base on client request implementation
		List<NameValuePair> headers = request.getHeaders();
		if (null != headers) {
			for (NameValuePair header : headers) {
				httpRequestBase.setHeader(header.getName(), header.getValue());
			}
		}
	}

	private static void buildEntity(
			final HttpEntityEnclosingRequestBase httpRequestBase,
			final RequestBuilder request) {
		// build Entity
		httpRequestBase.setEntity(request.getEntity());
	}

}
