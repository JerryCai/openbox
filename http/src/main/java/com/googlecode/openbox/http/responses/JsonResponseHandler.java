package com.googlecode.openbox.http.responses;

import com.googlecode.openbox.http.AbstractResponseHandler;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class JsonResponseHandler<T> extends
        AbstractResponseHandler<JsonResponse<T>> {

    private Class<T> classT;

    public JsonResponseHandler(Class<T> classT) {
        this.classT = classT;

    }

    public static <T> JsonResponseHandler<T> newInstance(Class<T> classT) {
        return new JsonResponseHandler(classT);
    }


    @Override
    public JsonResponse<T> handleResponse(HttpResponse response)
            throws IOException {
        try {
            return new JsonResponse<T>(response, getHttpContext(),
                    getExecutorMonitorManager(), classT);
        } catch (Exception e) {
            return null;
        }
    }
}
