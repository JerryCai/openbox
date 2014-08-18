package com.googlecode.openbox.http.requests;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.http.Request;

public abstract class XmlApiRequest extends Request {
	private static final Logger logger = LogManager.getLogger();

	public XmlApiRequest(String url) {
		super(url);
	}

	public XmlApiRequest(String scheme, String host, int port, String path) {
		super(scheme, host, port, path);
	}

	public abstract <T> T getXmlObject();

	public abstract String getXmlSchemaLocation();
	
	@Override
	public HttpEntity getEntity() {
		String xmlContent = "";
		if (null != getXmlObject()) {
			StringWriter writer = new StringWriter();
			try {
				JAXBContext jaxbContext = JAXBContext
						.newInstance(getXmlObject().getClass());

				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				if(!StringUtils.isBlank(getXmlSchemaLocation())){
					marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, getXmlSchemaLocation());
				}		        
				marshaller.marshal(getXmlObject(), writer);
				xmlContent = writer.toString();
			} catch (JAXBException e) {
				throw new RuntimeException(
						"JAXB marshal XMLAPI content Object error ! ", e);
			}

		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("This request's implementation method <T> T getXmlObject() return null object , so use empty StringEntity as HttpEntity");
			}
		}

		return EntityBuilder.create().setText(xmlContent)
				.setContentType(ContentType.APPLICATION_XML).build();
	}
}
