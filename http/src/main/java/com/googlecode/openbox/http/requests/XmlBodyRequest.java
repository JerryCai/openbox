package com.googlecode.openbox.http.requests;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.dom4j.Document;

import com.googlecode.openbox.http.Request;

public abstract class XmlBodyRequest extends Request {

	Document xmlBody;

	public XmlBodyRequest(String url) {
		super(url);
	}

	public XmlBodyRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	public abstract Document getXmlBody();

	@Override
	public HttpEntity getEntity() {
		return EntityBuilder.create().setText(getXmlBody().asXML())
				.setContentType(ContentType.APPLICATION_XML).build();
	}
}
