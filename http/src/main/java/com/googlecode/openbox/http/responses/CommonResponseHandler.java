package com.googlecode.openbox.http.responses;

import com.googlecode.openbox.http.AbstractResponseHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;

public class CommonResponseHandler extends
        AbstractResponseHandler<CommonResponse> {


    public CommonResponseHandler() {
    }

    public static CommonResponseHandler newInstance() {
        return new CommonResponseHandler();
    }

    @Override
    public CommonResponse handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {

        return new CommonResponse(response, getHttpContext(),
                getExecutorMonitorManager());
    }

}
