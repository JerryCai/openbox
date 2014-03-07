package com.google.test.openbox.http.httpbuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;

import com.google.test.openbox.common.XmlUtils;
import com.google.test.openbox.http.ExecutorMonitorManager;
import com.google.test.openbox.http.ExecutorMonitorManagerImpl;
import com.google.test.openbox.http.HttpClientException;
import com.google.test.openbox.http.HttpRequestFactory;
import com.google.test.openbox.http.TimeLine;
import com.google.test.openbox.http.monitors.TimeLineMonitor;

public class HttpBuilder {
	private static final Logger logger = LogManager.getLogger();

	public static final CloseableHttpClient DEFAULT_HTTPCLIENT = HttpClients
			.custom().setMaxConnTotal(1000).setMaxConnPerRoute(1000).build();

	private String method;
	private URI uri;
	private List<NameValuePair> parameters;
	private List<NameValuePair> headers;
	private List<NameValuePair> forms;
	private HttpEntity requestEntity;
	private URIBuilder uriBuilder;
	private EntityBuilder entityBuilder;
	private boolean hasForm;
	private boolean hasOtherBody;
	private Response response;
	private ExecutorMonitorManager executorMonitorManager;
	private CloseableHttpClient customHttpClient;

	private HttpBuilder() {
		this.uriBuilder = new URIBuilder();
		this.entityBuilder = EntityBuilder.create();
		this.hasForm = false;
		this.hasOtherBody = false;
		this.uri = null;
		this.requestEntity = null;
		this.executorMonitorManager = ExecutorMonitorManagerImpl.newInstance();
	}

	private HttpBuilder(CloseableHttpClient customHttpClient) {
		this();
		this.customHttpClient = customHttpClient;

	}

	public static HttpBuilder create() {
		return new HttpBuilder();
	}

	public static HttpBuilder create(CloseableHttpClient customHttpClient) {
		return new HttpBuilder(customHttpClient);
	}

	public HttpBuilder setMethod(String method) {
		this.method = method;
		return this;
	}

	public HttpBuilder setUrl(final String url) {
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			throw HttpClientException.create("build your url [" + url
					+ "] error !", e);
		}
		uriBuilder.setScheme(uri.getScheme());
		uriBuilder.setHost(uri.getHost());
		uriBuilder.setPort(uri.getPort());
		uriBuilder.setUserInfo(uri.getUserInfo());
		uriBuilder.setPath(uri.getPath());
		parameters = parseQuery(uri.getRawQuery(), Consts.UTF_8);
		uriBuilder.setFragment(uri.getFragment());
		return this;
	}

	private List<NameValuePair> parseQuery(final String query,
			final Charset charset) {
		if (query != null && query.length() > 0) {
			return URLEncodedUtils.parse(query, charset);
		}
		return null;
	}

	public HttpBuilder setScheme(final String scheme) {
		uriBuilder.setScheme(scheme);
		return this;
	}

	public HttpBuilder setUserInfo(final String userInfo) {
		uriBuilder.setUserInfo(userInfo);
		return this;
	}

	public HttpBuilder setUserInfo(final String username, final String password) {
		return setUserInfo(username + ':' + password);
	}

	public HttpBuilder setHost(final String host) {
		uriBuilder.setHost(host);
		return this;
	}

	public HttpBuilder setPort(final int port) {
		uriBuilder.setPort(port);
		return this;
	}

	public HttpBuilder setPath(final String path) {
		uriBuilder.setPath(path);
		return this;
	}

	public HttpBuilder setFragment(final String fragment) {
		uriBuilder.setFragment(fragment);
		return this;
	}

	public HttpBuilder addParameter(String name, String value) {
		if (null == parameters) {
			parameters = new LinkedList<NameValuePair>();
		}
		add(parameters, name, value);
		return this;
	}

	public HttpBuilder setParameter(String name, String value) {
		if (null == parameters) {
			parameters = new LinkedList<NameValuePair>();
		}
		set(parameters, name, value);
		return this;
	}

	public HttpBuilder removeParameter(String name) {
		remove(parameters, name);
		return this;
	}

	public HttpBuilder addHeader(String name, String value) {
		if (null == headers) {
			headers = new LinkedList<NameValuePair>();
		}
		add(headers, name, value);
		return this;
	}

	public HttpBuilder setHeader(String name, String value) {
		if (null == headers) {
			headers = new LinkedList<NameValuePair>();
		}
		set(headers, name, value);
		return this;
	}

	public HttpBuilder removeHeader(String name) {
		remove(headers, name);
		return this;
	}

	public HttpBuilder addForm(String name, String value) {
		if (null == forms) {
			forms = new LinkedList<NameValuePair>();
		}
		add(forms, name, value);
		hasForm = true;
		return this;
	}

	public HttpBuilder setForm(String name, String value) {
		if (null == forms) {
			forms = new LinkedList<NameValuePair>();
		}
		set(forms, name, value);
		hasForm = true;
		return this;
	}

	public HttpBuilder removeForm(String name) {
		remove(forms, name);
		if (null == forms || forms.size() <= 0) {
			hasForm = false;
		}
		return this;
	}

	public HttpBuilder setText(final String text) {
		entityBuilder.setText(text);
		hasOtherBody = true;
		return this;
	}

	public HttpBuilder setBinary(final byte[] binary) {
		entityBuilder.setBinary(binary);
		hasOtherBody = true;
		return this;
	}

	public HttpBuilder setStream(final InputStream stream) {
		entityBuilder.setStream(stream);
		hasOtherBody = true;
		return this;
	}

	public HttpBuilder setSerializable(final Serializable serializable) {
		entityBuilder.setSerializable(serializable);
		hasOtherBody = true;
		return this;
	}

	public HttpBuilder setFile(final File file) {
		entityBuilder.setFile(file);
		hasOtherBody = true;
		return this;
	}

	public HttpBuilder setContentType(final ContentType contentType) {
		entityBuilder.setContentType(contentType);
		return this;
	}

	public HttpBuilder setContentEncoding(final String contentEncoding) {
		entityBuilder.setContentEncoding(contentEncoding);
		return this;
	}

	public HttpBuilder chunked() {
		entityBuilder.chunked();
		return this;
	}

	public HttpBuilder gzipCompress() {
		entityBuilder.gzipCompress();
		return this;
	}

	public HttpRequestBase build() {
		if (null != parameters && parameters.size() > 0) {
			uriBuilder.addParameters(parameters);
		}

		if (hasForm) {
			entityBuilder.setParameters(forms);
		}

		HttpRequestBase httpRequest = null;
		if (hasBody()) {
			httpRequest = HttpRequestFactory.createHasBodyRequest(method);
		} else {
			httpRequest = HttpRequestFactory.createNoBodyRequestBase(method);
		}

		try {
			this.uri = uriBuilder.build();
			httpRequest.setURI(uri);
		} catch (URISyntaxException e) {
			throw HttpClientException.create(
					"check your request uri error by URIBuilder !", e);
		}
		if (null != headers) {
			for (NameValuePair header : headers) {
				httpRequest.addHeader(header.getName(), header.getValue());
			}
		}
		if (hasBody()) {
			requestEntity = entityBuilder.build();
			((HttpEntityEnclosingRequestBase) httpRequest)
					.setEntity(requestEntity);

		}
		return httpRequest;
	}

	private boolean hasBody() {
		return hasForm || hasOtherBody;
	}

	private CloseableHttpClient getHttpClient() {
		if (null != customHttpClient) {
			return customHttpClient;
		}
		return DEFAULT_HTTPCLIENT;
	}

	public Response execute() {
		try {
			HttpRequestBase request = build();
			if (logger.isInfoEnabled()) {
				logger.info(getRequestLog());
			}
			registerDefaultMonitors();
			executorMonitorManager.startMonitors();
			CloseableHttpResponse httpResponse = null;
			try {
				httpResponse = getHttpClient().execute(request);
				if (null == httpResponse) {
					throw HttpClientException
							.create("http builder execute failed as response object is null after execute");
				}
			} finally {
				executorMonitorManager.endMonitors();
			}
			this.response = Response.from(httpResponse, executorMonitorManager);
			if (logger.isInfoEnabled()) {
				logger.info(getResponseLog());
			}
			return response;

		} catch (Exception e) {
			throw HttpClientException.create("send your request error !", e);
		}
	}

	public <T> T execute(HttpBuilderResponseHandler<? extends T> responseHandler) {
		execute();
		if (null == response) {
			throw HttpClientException
					.create("response is parse is error as null before call HttpBuilderResponseHandler");
		}
		return responseHandler.handlerResponse(response);
	}

	public ExecutorMonitorManager getExecutorMonitorManager() {
		return executorMonitorManager;
	}

	public interface HttpBuilderResponseHandler<T> {
		T handlerResponse(Response response);
	}

	public static class HttpBuilderXmlResponseHanlder implements
			HttpBuilderResponseHandler<Document> {
		public HttpBuilderXmlResponseHanlder() {
		}

		public static HttpBuilderXmlResponseHanlder create() {
			return new HttpBuilderXmlResponseHanlder();
		}

		@Override
		public Document handlerResponse(Response response) {
			return XmlUtils.buildXMLFromStringContent(response.getContent());
		}
	}

	private void registerDefaultMonitors() {
		executorMonitorManager.register(TimeLineMonitor.create());
	}

	private void add(List<NameValuePair> list, String name, String value) {
		list.add(new BasicNameValuePair(name, value));
	}

	private void set(List<NameValuePair> list, String name, String value) {

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

	private void remove(List<NameValuePair> list, String name) {
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

	private String getRequestLog() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\n===================================== [T-"
					+ Thread.currentThread().getId()
					+ " Request ]==============================================\n");
			sb.append(method).append("\n").append(uri).append("\n");
			sb.append("------------------------headers--------------------------\n");
			if (null != headers) {
				for (NameValuePair header : headers) {
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

	private String getResponseLog() {
		if (null == response) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\n=======================================[T-"
					+ Thread.currentThread().getId()
					+ " Response ]============================================\n");
			sb.append("Status Line -->[").append(
					this.response.getStatusLine() + "]\n");
			sb.append("Time Line -->" + response.getTimeLine().toString());
			sb.append("\n------------------------ response headers --------------------------\n");
			Header[] responseHeaders = this.response.getAllHeaders();
			if (null != responseHeaders) {
				for (Header header : responseHeaders) {
					sb.append(header.getName()).append(":")
							.append(header.getValue()).append("\n");
				}
			}
			sb.append("\n------------------------ response content --------------------------\n");
			String content = response.getContent();
			if (null != content) {
				sb.append(content);
			}
			sb.append("\n=================================================================================================\n");
			return sb.toString();
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public static final class Response {
		private ExecutorMonitorManager executorMonitorManager;
		private CloseableHttpResponse httpResponse;
		private String content;

		private Response(CloseableHttpResponse httpResponse,
				ExecutorMonitorManager executorMonitorManager) {
			this.httpResponse = httpResponse;
			this.executorMonitorManager = executorMonitorManager;
			if (null != this.httpResponse) {
				try {
					this.content = EntityUtils.toString(
							httpResponse.getEntity(), "UTF-8");
				} catch (Exception e) {
					throw HttpClientException
							.create("get http builder executed http response content error !",
									e);
				} finally {
					try {
						this.httpResponse.close();
					} catch (IOException e) {
						logger.error(e);
					}
				}
			}
		}

		public static Response from(CloseableHttpResponse httpResponse,
				ExecutorMonitorManager executorMonitorManager) {
			return new Response(httpResponse, executorMonitorManager);
		}

		public TimeLine getTimeLine() {
			TimeLineMonitor tlm = (TimeLineMonitor) executorMonitorManager
					.getMonitor(TimeLineMonitor.NAME);
			return tlm.toTimeLine();
		}

		public String getContent() {
			return content;
		}

		public ProtocolVersion getProtocolVersion() {
			return httpResponse.getProtocolVersion();
		}

		public boolean containsHeader(String name) {
			return httpResponse.containsHeader(name);
		}

		public Header[] getHeaders(String name) {
			return httpResponse.getHeaders(name);
		}

		public Header getFirstHeader(String name) {
			return httpResponse.getFirstHeader(name);
		}

		public Header getLastHeader(String name) {
			return httpResponse.getLastHeader(name);
		}

		public Header[] getAllHeaders() {
			return httpResponse.getAllHeaders();
		}

		public HeaderIterator headerIterator() {
			return httpResponse.headerIterator();
		}

		public HeaderIterator headerIterator(String name) {
			return httpResponse.headerIterator(name);
		}

		public StatusLine getStatusLine() {
			return httpResponse.getStatusLine();
		}

	}
}
