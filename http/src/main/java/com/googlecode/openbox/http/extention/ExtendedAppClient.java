package com.googlecode.openbox.http.extention;

import com.googlecode.openbox.http.*;
import com.googlecode.openbox.http.responses.JsonResponse;
import com.googlecode.openbox.http.responses.JsonResponseHandler;
import com.googlecode.openbox.http.responses.StringResponse;
import com.googlecode.openbox.http.responses.StringResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.LinkedList;
import java.util.List;

public abstract class ExtendedAppClient implements AppClientExtendable {
    private CloseableHttpClient httpClient;
    private List<RequestProxy> mcProxys;
    private List<ExecutorMonitor> monitors;

    public ExtendedAppClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
        this.mcProxys = new LinkedList<RequestProxy>();
        this.monitors = new LinkedList<ExecutorMonitor>();
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public void registerProxy(RequestProxy proxy) {
        mcProxys.add(proxy);
    }

    @Override
    public void unregisterProxy(RequestProxy proxy) {
        mcProxys.remove(proxy);

    }

    @Override
    public void registerMonitor(ExecutorMonitor monitor) {
        monitors.add(monitor);
    }

    @Override
    public void unregisterMonitor(ExecutorMonitor monitor) {
        monitors.remove(monitor);
    }

    @Override
    public void cleanAllProxy() {
        mcProxys.clear();
    }

    @Override
    public void cleanAllMonitor() {
        monitors.clear();

    }

    public void registerAllPlugins(final HttpExecutor<?> httpExecutor) {
        for (RequestProxy mcProxy : mcProxys) {
            httpExecutor.registerHttpRequestProxy(mcProxy);
        }
        httpExecutor.getExecutorMonitorManager().register(monitors);
    }


    public <T> JsonResponse<T> sendJsonResponseRequest(Request request, Class<T> responseTypeClass) {
        return sendRequest(request, new JsonResponseHandler<T>(responseTypeClass));
    }


    public StringResponse sendRequest(Request request) {
        return sendRequest(request, new StringResponseHandler());
    }

    public <T extends Response> T sendRequest(Request request, AbstractResponseHandler<T> responseHandler) {
        HttpExecutor<T> httpExecutor = new DefaultHttpExecutor<>(getHttpClient(), request, responseHandler);
        try {
            registerAllPlugins(httpExecutor);
            httpExecutor.execute();
            return httpExecutor.getResponse();
        } catch (Exception e) {
            throw new HttpClientException("execute [" + request.getClass().getSimpleName() + "] error !", e);
        }
    }
}
