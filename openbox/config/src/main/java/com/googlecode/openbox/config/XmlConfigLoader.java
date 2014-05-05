package com.googlecode.openbox.config;

import java.io.InputStream;
import java.util.Map;

import org.dom4j.Document;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.XmlUtils;

public class XmlConfigLoader {

	private Document xml;
	@SuppressWarnings("rawtypes")
	private Map namespaces = null;

	public XmlConfigLoader(String path) {
		this(IOUtils.getInputStreamByProjectRelativePath(path));
	}

	@SuppressWarnings("rawtypes")
	public void setNamespaces(Map namespaces) {
		this.namespaces = namespaces;
	}

	public XmlConfigLoader(InputStream xmlStrem) {
		this.xml = XmlUtils.buildXML(xmlStrem);
	}

	public String getConfigItem(String xPath) {
		if (null == namespaces)
			return XmlUtils.querySingleXPath(xml, xPath);
		return XmlUtils.querySingleXPath(xml, xPath, namespaces);
	}

	public Document getConfigDocument() {
		return xml;
	}

}
