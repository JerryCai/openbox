package com.google.test.openbox.http;

import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractRequestBuilder implements RequestBuilder {
	private static final Logger logger = LogManager.getLogger();

	public static final String CHARSET_UTF_8 = Consts.UTF_8.name();

	private URIBuilder builder;
	private List<NameValuePair> headers = null;

	public AbstractRequestBuilder(URIBuilder builder) {
		this.builder = builder;
	}

	public AbstractRequestBuilder(String url) {
		try {
			builder = new URIBuilder(url);
		} catch (URISyntaxException e) {
			String msg = "your url: [" + url
					+ "] is invalid format ,Please check it";
			logger.error(msg, e);
			throw new HttpClientException(msg, e);
		}
	}

	public AbstractRequestBuilder(String url, String path) {
		try {
			builder = new URIBuilder(url);
		} catch (URISyntaxException e) {
			String msg = "your url: [" + url
					+ "] is invalid format ,Please check it";
			logger.error(msg, e);
			throw new HttpClientException(msg, e);
		}
		builder.setPath(path);
	}

	public AbstractRequestBuilder(String scheme, String host, String path) {
		builder = new URIBuilder();
		builder.setScheme(scheme);
		builder.setHost(host);
		builder.setPath(path);

	}

	public AbstractRequestBuilder(String scheme, String host, int port,
			String path) {
		builder = new URIBuilder();
		builder.setScheme(scheme);
		builder.setHost(host);
		builder.setPort(port);
		builder.setPath(path);
	}

	@Override
	public URIBuilder getURIBuilder() {
		return builder;
	}

	@Override
	public RequestConfig getRequestConfig() {
		return null;
	}

	@Override
	public List<NameValuePair> getHeaders() {
		return headers;
	}

	@Override
	public HttpRequestBase toRequest() {
		return RequestConverter.toHttpRequestBase(this);
	}

	public void addHeader(String name, String value) {
		if (null == headers) {
			headers = new LinkedList<NameValuePair>();
		}
		add(headers, name, value);
	}

	public void setHeader(String name, String value) {
		if (null == headers) {
			headers = new LinkedList<NameValuePair>();
		}
		set(headers, name, value);
	}

	public void removeHeader(String name) {
		remove(headers, name);
	}

	protected void add(List<NameValuePair> list, String name, String value) {
		list.add(new BasicNameValuePair(name, value));
	}

	protected void set(List<NameValuePair> list, String name, String value) {

		boolean isFind = false;
		for (int index = 0; index < list.size(); index++) {
			NameValuePair header = list.get(index);
			if (header.getName().equals(name)) {
				list.set(index, new BasicNameValuePair(name, value));
				isFind = true;
				break;
			}
		}
		if (!isFind) {
			list.add(new BasicNameValuePair(name, value));
		}
	}

	protected void remove(List<NameValuePair> list, String name) {
		if (null == list) {
			return;
		}

		for (int index = 0; index < list.size(); index++) {
			NameValuePair header = list.get(index);
			if (header.getName().equals(name)) {
				list.remove(index);
				break;
			}
		}
	}

}
