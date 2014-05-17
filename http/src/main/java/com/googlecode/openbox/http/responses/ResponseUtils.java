package com.googlecode.openbox.http.responses;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ResponseUtils {
	private static final Logger logger = LogManager.getLogger();

	private ResponseUtils() {
	}

	public static int getStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();
	}

	public static Header[] getAllHeaders(HttpResponse response) {
		return response.getAllHeaders();
	}

	public static String getStringContent(HttpResponse response) {

		final HttpEntity entity = response.getEntity();
		try {
			return entity == null ? null : EntityUtils.toString(entity);
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn(
						"get string content from http response error , so return null ",
						e);
			}
		} finally {
			EntityUtils.consumeQuietly(entity);
		}
		return null;

	}

	public static void assertContentType(ContentType[] supportedTypes,
			HttpResponse httpResponse) {
//		if (null == supportedTypes) {
//			// if supportedTypes is null , it means support any content type or
//			// null content type
//			return;
//		}
//		HttpEntity entity = httpResponse.getEntity();
//		if (null != entity) {
//			Header contentTypeHeader = entity.getContentType();
//			if (null == contentTypeHeader) {
//				throw new HttpClientException(
//						"HttpResponse hasn't found contentType header , Please check response");
//			}
//
//			String contentType = contentTypeHeader.getValue();
//			if (null == contentType || "".equals(contentType.trim())) {
//				throw new HttpClientException(
//						"HttpResponse contentType value is null or empty , Please check response");
//			}
//
//			boolean notFound = true;
//			String[] rct = contentType.split(";");
//			if(rct.length != 2){
//				throw new HttpClientException(
//						"HttpResponse contentType value is error , Please check response");
//			}
//			String[] charsets = rct[1].split("=");
//			if(charsets.length !=2){
//				throw new HttpClientException(
//						"HttpResponse contentType value is error , Please check response");
//			}
//			ContentType resposneContentType = ContentType.create(rct[0].trim(), charsets[1].trim());
//
//			
//			String supportTypes = "[";
//			for (ContentType supportedType : supportedTypes) {
//				String mineType = supportedType.getMimeType();
//				supportTypes = supportTypes + " , ";
//				if (mineType.equals(resposneContentType.getMimeType())) {
//					notFound = false;
//					break;
//				}
//			}
//			if (notFound) {
//				String msg = "This request response just support parse ["
//						+ supportTypes + "] , but actual ContentType is ["
//						+ contentType + "], Please check response";
//				throw new HttpClientException(msg);
//			}
//
//		}
	}

}
