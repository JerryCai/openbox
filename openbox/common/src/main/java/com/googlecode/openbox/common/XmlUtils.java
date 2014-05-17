package com.googlecode.openbox.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

public class XmlUtils {

	private static final Logger logger = LogManager.getLogger();

	public static Document buildEmptyXml() {
		return DocumentHelper.createDocument();
	}

	public static org.w3c.dom.Document builderDocument(File file) {
		DocumentBuilderFactory domBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = domBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error(
					" new document builder failed from document builder factory",
					e);
			throw new RuntimeException(e);
		}
		org.w3c.dom.Document doc = null;
		try {
			doc = builder.parse(file);
		} catch (SAXException e) {
			logger.error("parse the document from file=[" + file
					+ "]failed , maybe this file content can't parseable !!!",
					e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error("parse the document from file=[" + file
					+ "]failed , maybe this file can't be found !!!", e);
			throw new RuntimeException(e);
		}
		return doc;
	}

	public static Document buildXMLFromStringContent(String xmlBody) {
		Document xml = null;
		SAXReader xmlBuilder = new SAXReader();
		InputStream instream = IOUtils.getStreamFromString(xmlBody);
		try {
			xml = xmlBuilder.read(instream);
		} catch (DocumentException e) {
			logger.error("Builder xml from input stream failed !!!", e);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeInputStream(instream);
		}
		return xml;
	}

	public static Document buildXML(InputStream instream) {
		if (null == instream) {
			throw new RuntimeException(
					"The input stream for builder xml is NULL , params error");
		}
		Document xml = null;
		SAXReader xmlBuilder = new SAXReader();
		try {
			xml = xmlBuilder.read(instream);
		} catch (DocumentException e) {
			logger.error("Builder xml from input stream failed !!!", e);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeInputStream(instream);
		}
		return xml;
	}

	public static Document buildXML(String xmlPath) {
		InputStream instream = null;
		try {
			instream = new FileInputStream(xmlPath);
			if (logger.isInfoEnabled()) {
				logger.info("begin to build xml from the xmlPath=[" + xmlPath
						+ "]...");
			}
		} catch (FileNotFoundException e) {
			String message = "build the xml from the xmlPath=[" + xmlPath
					+ "] failed!!!";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
		return buildXML(instream);
	}

	public static boolean exportXmlToFile(Document xml, String localFilePath) {
		boolean isSuccess = true;
		// if local path is not existed , create it automatically
		File file = new File(localFilePath);
		File parentFolder = new File(file.getParent());
		parentFolder.mkdirs();
		parentFolder.setReadable(true);
		parentFolder.setWritable(true);
		// export the xml as formatted to local file
		XMLWriter output;
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			output = new XMLWriter(new FileWriter(localFilePath), format);
			output.write(xml);
			output.close();
			if (logger.isInfoEnabled()) {
				logger.info("Export xml document to local file:["
						+ localFilePath + "] successfully!! ");
			}
		} catch (IOException e) {
			isSuccess = false;
			logger.error("Export xml document to local file:[" + localFilePath
					+ "] failed!! ", e);
		}
		return isSuccess;
	}

	/**
	 * 
	 * @param xml
	 * @param xPath
	 * @param namespaces
	 * @return
	 */
	public static String querySingleXPath(Document xml, String xPath,
			@SuppressWarnings("rawtypes") Map namespaces) {
		xPath = xPath.replaceAll("/", "//");
		XPath xPathObj = xml.createXPath(xPath);
		xPathObj.setNamespaceURIs(namespaces);
		String value = null;
		Node node = xPathObj.selectSingleNode(xml);
		if (null != node) {
			value = node.getText();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("xPath=[" + xPath + "] , value=[" + value + "]");
		}
		return value;
	}

	public static String querySingleXPath(Element element, String xPath,
			@SuppressWarnings("rawtypes") Map namespaces) {
		xPath = xPath.replaceAll("/", "//");
		XPath xPathObj = element.createXPath(xPath);
		xPathObj.setNamespaceURIs(namespaces);
		String value = null;
		Node node = xPathObj.selectSingleNode(element);
		if (null != node) {
			value = node.getText();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("xPath=[" + xPath + "] , value=[" + value + "]");
		}
		return value;
	}

	@SuppressWarnings("rawtypes")
	public static List queryXPath(Element element, String xPath, Map namespaces) {
		xPath = xPath.replaceAll("/", "//");
		XPath xPathObj = element.createXPath(xPath);
		xPathObj.setNamespaceURIs(namespaces);
		return xPathObj.selectNodes(element);
	}

	@SuppressWarnings("rawtypes")
	public static List queryXPath(Document dom, String xPath, Map namespaces) {
		xPath = xPath.replaceAll("/", "//");
		XPath xPathObj = dom.createXPath(xPath);
		if (null != namespaces) {
			xPathObj.setNamespaceURIs(namespaces);
		}
		return xPathObj.selectNodes(dom);
	}

	public static String querySingleXPath(Document xml, String xPath) {
		String value = null;
		try {
			Node node = xml.selectSingleNode(xPath);
			value = node.getStringValue();
			if (logger.isDebugEnabled()) {
				logger.debug("xPath=[" + xPath + "] , value=[" + value + "]");
			}
		} catch (Exception e) {
			String msg = "xPath=[" + xPath + "] can not be found";
			logger.error(msg, e);
			throw new RuntimeException(msg, e);
		}
		return value;
	}

	@SuppressWarnings("rawtypes")
	public static List queryXPath(Document xml, String xPath) {
		List value = null;
		try {
			String[] xPaths = xPath.substring(1).split("/");
			int length = xPaths.length;
			Element rootElement = xml.getRootElement();
			for (int i = 1; i < length - 1; i++) {
				if (null == rootElement) {
					return null;
				}
				rootElement = rootElement.element(xPaths[i]);
			}
			if (null == rootElement) {
				return null;
			}
			value = rootElement.elements();
			if (logger.isDebugEnabled()) {
				logger.debug("select xPath=[" + xPath + "] totally search ["
						+ value.size() + "] Items");
			}
		} catch (Exception e) {
			logger.error("xPath=[" + xPath + "] can not be found", e);
		}
		return value;
	}

	public static String querySingleXPath(Element element, String xPath) {
		String value = null;
		try {
			String[] xPaths = xPath.substring(1).split("/");
			int length = xPaths.length;
			Element rootElement = element;
			for (int i = 0; i < length; i++) {
				if (null == rootElement) {
					return null;
				}
				rootElement = rootElement.element(xPaths[i]);
			}
			if (null == rootElement) {
				return null;
			}
			value = rootElement.getTextTrim();
			if (logger.isDebugEnabled()) {
				logger.debug("select xPath=[" + xPath + "] totally search ["
						+ value + "] Items");
			}
		} catch (Exception e) {
			logger.error("xPath=[" + xPath + "] can not be found", e);
		}
		return value;
	}

	@SuppressWarnings("rawtypes")
	public static List queryXPath(Element element, String xPath) {
		List value = null;
		try {
			String[] xPaths = xPath.substring(1).split("/");
			int length = xPaths.length;
			Element rootElement = element;
			for (int i = 1; i < length - 1; i++) {
				if (null == rootElement) {
					return null;
				}
				rootElement = rootElement.element(xPaths[i]);
			}
			if (null == rootElement) {
				return null;
			}
			value = rootElement.elements();
			if (logger.isDebugEnabled()) {
				logger.debug("select xPath=[" + xPath + "] totally search ["
						+ value.size() + "] Items");
			}
		} catch (Exception e) {
			logger.error("xPath=[" + xPath + "] can not be found", e);
		}
		return value;
	}

	public static void printXml(Document xml) {
		if (logger.isInfoEnabled()) {
			logger.info("\n\r" + xml.asXML() + "\n\r");
		}
	}
}
