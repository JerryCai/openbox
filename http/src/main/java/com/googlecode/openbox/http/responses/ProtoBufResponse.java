package com.googlecode.openbox.http.responses;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.openbox.http.AbstractResponse;
import com.googlecode.openbox.http.ExecutorMonitorManager;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;

/**
 * Created by jerrycai on 16-11-4.
 */
public abstract class ProtoBufResponse<T extends GeneratedMessageLite> extends AbstractResponse {

    private T t;

    public ProtoBufResponse(HttpResponse httpResponse, HttpContext httpContext, ExecutorMonitorManager executorMonitorManager) {
        super(httpResponse, httpContext, executorMonitorManager);
        try {
            this.t = parseFrom(getBodyData());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("parse the protobuf response data error", e);
        }
    }

    protected abstract T parseFrom(byte[] responseProbufData) throws InvalidProtocolBufferException;


    public T getProtoBufResponse() {
        return t;
    }

    @Override
    public ContentType[] getSupportedContentTypes() {
        return new ContentType[]{ContentType.create("application/x-protobuf", Consts.UTF_8)};

    }
}
