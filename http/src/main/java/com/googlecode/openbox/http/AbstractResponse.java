package com.googlecode.openbox.http;

import com.googlecode.openbox.http.monitors.TimeLineMonitor;
import com.googlecode.openbox.http.responses.ResponseUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractResponse implements Response {
    private static final Logger logger = LogManager.getLogger();

    private HttpResponse httpResponse;
    private byte[] data;
    private String content;
    @SuppressWarnings("unused")
    private HttpContext httpContext; // this is used for extend in future .
    private ExecutorMonitorManager executorMonitorManager;

    public AbstractResponse(HttpResponse httpResponse, HttpContext httpContext,
                            ExecutorMonitorManager executorMonitorManager) {
        this.httpResponse = httpResponse;
        this.httpContext = httpContext;
        this.executorMonitorManager = executorMonitorManager;

        HttpEntity entity = getHttpResponse().getEntity();
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        if (null != entity) {
            try {
                entity.writeTo(buf);
                this.data = buf.toByteArray();
                this.content = new String(this.data);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
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

    @Override
    public String getHeaderValue(String headerName) {
        for (Header header : getHeaders()) {
            if (header.getName().equals(headerName)) {
                return header.getValue();
            }
        }
        return null;
    }

    private HttpResponse getHttpResponse() {
        return httpResponse;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public byte[] getBodyData() {
        return data;
    }

    public static ContentType getUtf8ContentType(ContentType contentType) {
        return ContentType.create(contentType.getMimeType(), Consts.UTF_8);
    }

    private String getResponseLog() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\n\n=======================================[T-" + Thread.currentThread().getId() + " Response ]============================================\n");
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
            if (null != getContent()) {
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
