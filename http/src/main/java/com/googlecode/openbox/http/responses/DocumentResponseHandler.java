package com.googlecode.openbox.http.responses;

import com.googlecode.openbox.http.AbstractResponseHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

public class DocumentResponseHandler extends AbstractResponseHandler<DocumentResponse> {


    public DocumentResponseHandler() {
    }

    public static DocumentResponseHandler newInstance() {
        return new DocumentResponseHandler();
    }

    @Override
    public DocumentResponse handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {
        return new DocumentResponse(response, getHttpContext(),
                getExecutorMonitorManager());
    }

}
