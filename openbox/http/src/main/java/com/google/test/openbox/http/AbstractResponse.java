package com.google.test.openbox.http;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.test.openbox.http.monitors.TimeLineMonitor;
import com.google.test.openbox.http.responses.ResponseUtils;

public abstract class AbstractResponse implements Response {
	private static final Logger logger = LogManager.getLogger();

	private HttpResponse httpResponse;
	private String content;
	@SuppressWarnings("unused")
	private HttpContext httpContext; // this is used for extend in future .
	private ExecutorMonitorManager executorMonitorManager;

	public AbstractResponse(HttpResponse httpResponse, HttpContext httpContext,
			ExecutorMonitorManager executorMonitorManager) {
		this.httpResponse = httpResponse;
		this.httpContext = httpContext;
		this.executorMonitorManager = executorMonitorManager;
		this.content = ResponseUtils.getStringContent(getHttpResponse());
		EntityUtils.consumeQuietly(getHttpResponse().getEntity());
		if (logger.isInfoEnabled()) {
			logger.info(getResponseLog());
		}
	}

	@Override
	public TimeLine getTimeLine() {
		TimeLineMonitor timeLineMonitor = (TimeLineMonitor) executorMonitorManager
				.getMonitor(TimeLineMonitor.NAME);
		if (null == timeLineMonitor) {
			throw HttpClientException
					.create("TimeLineMonitor monitor should be default required monitor , but we can't get it , please check code !");
		}
		return timeLineMonitor.toTimeLine();
	}

	@Override
	public int getStatusCode() {
		return ResponseUtils.getStatusCode(httpResponse);
	}

	@Override
	public Header[] getHeaders() {
		return ResponseUtils.getAllHeaders(httpResponse);
	}

	private HttpResponse getHttpResponse() {
		return httpResponse;
	};

	@Override
	public String getContent() {
		return content;
	}

	public static ContentType getUtf8ContentType(ContentType contentType) {
		return ContentType.create(contentType.getMimeType(), Consts.UTF_8);
	}

	private String getResponseLog() {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("\n\n=======================================[T-"+Thread.currentThread().getId()+ " Response ]============================================\n");
			sb.append("status code -->[").append(getStatusCode() + "]");
			sb.append("\n------------------------ response headers --------------------------\n");
			Header[] responseHeaders = getHeaders();
			if (null != responseHeaders) {
				for (Header header : responseHeaders) {
					sb.append(header.getName()).append(":")
							.append(header.getValue()).append("\n");
				}
			}
			sb.append("\n------------------------ response content --------------------------\n");
			if(null != getContent()){
				sb.append(getContent());
			}
			sb.append("\n=================================================================================================\n");
			return sb.toString();
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}

	}

	public abstract ContentType[] getSupportedContentTypes();

}
