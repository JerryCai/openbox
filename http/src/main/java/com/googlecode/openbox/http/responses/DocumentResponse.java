package com.googlecode.openbox.http.responses;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HttpContext;
import org.dom4j.Document;

import com.googlecode.openbox.common.XmlUtils;
import com.googlecode.openbox.http.AbstractResponse;
import com.googlecode.openbox.http.ExecutorMonitorManager;

public class DocumentResponse extends AbstractResponse {

	private Document doc;

	public DocumentResponse(HttpResponse httpResponse, HttpContext httpContext,
			ExecutorMonitorManager executorMonitorManager) {
		super(httpResponse, httpContext, executorMonitorManager);

		if (null != getContent()) {
			this.doc = XmlUtils.buildXMLFromStringContent(getContent());
		}
	}

	public Document getDocument() {
		return doc;
	}

	@Override
	public ContentType[] getSupportedContentTypes() {
		return new ContentType[] { getUtf8ContentType(ContentType.TEXT_XML),
				getUtf8ContentType(ContentType.APPLICATION_XML),
				getUtf8ContentType(ContentType.APPLICATION_ATOM_XML),
				getUtf8ContentType(ContentType.APPLICATION_SVG_XML),
				getUtf8ContentType(ContentType.APPLICATION_XHTML_XML),
				getUtf8ContentType(ContentType.TEXT_PLAIN) };
	}

}
