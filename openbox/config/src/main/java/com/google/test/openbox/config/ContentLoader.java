package com.google.test.openbox.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.test.openbox.common.IOUtils;

public class ContentLoader {

	private static final Map<String, String> SMALL_CONTENT_CACHE = new HashMap<String, String>();

	private ContentLoader() {
	}

	public static String getContent(String projectRelativePath) {
		InputStream contentStream = null;
		try{
			if (!SMALL_CONTENT_CACHE.containsKey(projectRelativePath)) {
				contentStream = IOUtils.getInputStreamByProjectRelativePath(projectRelativePath);
				String content = IOUtils.getStringFromStream(contentStream);
				if (content.length() > 512) {
					// if file content is large more than 1M , not to cache it
					return content;
				}
				SMALL_CONTENT_CACHE.put(projectRelativePath, content);
			}
		}catch(Exception e ){
			throw new RuntimeException(e);
		}finally{
			IOUtils.closeInputStream(contentStream);
		}
		return SMALL_CONTENT_CACHE.get(projectRelativePath);
	}
}
