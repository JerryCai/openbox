package com.googlecode.openbox.http.requests;

import com.google.protobuf.GeneratedMessageLite;
import com.googlecode.openbox.http.Request;
import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;

import java.io.ByteArrayInputStream;

/**
 * Created by jerrycai on 16-11-4.
 */
public abstract class ProtoBufRequest extends Request {

    public ProtoBufRequest(String url) {
        super(url);
        addHeader("Content-Type", "application/x-protobuf");
        addHeader("Accept", "application/x-protobuf");
    }

    public ProtoBufRequest(String scheme, String host, int port, String path) {
        super(scheme, host, port, path);
    }

    public abstract GeneratedMessageLite.Builder getProtobufBuilder();

    @Override
    public HttpEntity getEntity() {
        GeneratedMessageLite.Builder builder = getProtobufBuilder();
        if (null == builder) {
            return null;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(builder.build().toByteArray());
        return new InputStreamEntity(inputStream);
    }
}
