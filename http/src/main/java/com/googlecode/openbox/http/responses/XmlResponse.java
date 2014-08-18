package com.googlecode.openbox.http.responses;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;

import com.googlecode.openbox.http.AbstractResponse;
import com.googlecode.openbox.http.ExecutorMonitorManager;
import com.googlecode.openbox.http.HttpClientException;

public class XmlResponse<T> extends AbstractResponse {
	private T t;

	@SuppressWarnings("unchecked")
	public XmlResponse(HttpResponse httpResponse, HttpContext httpContext,
			ExecutorMonitorManager executorMonitorManager, Class<T> classT) {
		super(httpResponse, httpContext, executorMonitorManager);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(classT);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			this.t = (T) unmarshaller.unmarshal(new StringReader(getContent()));
		} catch (JAXBException e) {
			throw HttpClientException.create(
					"It can't parse response content to response object", e);
		}
	}

	public T getResponseObject() {
		return t;
	}

	@Override
	public ContentType[] getSupportedContentTypes() {
		return null;
	}

}
