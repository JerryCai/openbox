package com.google.test.openbox.jsonpool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.test.openbox.common.IOUtils;

class DefaultJsonStoreLoader<K, V> extends AbstractJsonStoreLoader<K, V> {
	private static final Logger logger = LogManager.getLogger();

	private JsonStoreProvider<K, V> provider;
	private Class<V> classT;

	public DefaultJsonStoreLoader(JsonStoreProvider<K, V> provider, Class<V> classT) {
		this.provider = provider;
		this.classT = classT;
	}

	@Override
	protected List<V> loadJsonObjectFromStore(K k) {

		List<V> list = new LinkedList<V>();
		FileReader fileReader = null;
		BufferedReader lineReader = null;
		try {
			String filePath = getStorePath(k);
			File file = new File(filePath);
			fileReader = new FileReader(file);
			lineReader = new BufferedReader(fileReader);
			String jsonString = null;
			while (null != (jsonString = lineReader.readLine())) {
				jsonString = jsonString.trim();
				if ("".equals(jsonString)) {
					continue;
				}
				V v =new Gson().fromJson(jsonString, TypeToken.get(classT).getType());
				if (null != v) {
					list.add(v);
				}
			}

		} catch (Exception e) {
			String message = "load stored Json from file "+getStorePath(k)+" encounter error !";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		} finally {
			if (null != lineReader) {
				try {
					lineReader.close();
					fileReader.close();
				} catch (IOException e) {
					logger.error("close file reader error .", e);
				}
			}
		}
		sumNum(k, list.size());
		return list;
	}

	@Override
	protected List<V> getJsonObjectsFromProvider(K k, int num) {
		List<V> newJsonObjs = provider.get(k, num,
				getCurrentNum(k));
		int actualNum = newJsonObjs.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < actualNum; i++) {
			sb.append("\n").append(new Gson().toJson(newJsonObjs.get(i)));
		}
		IOUtils.appendContentToFile(getStorePath(k), sb.toString());
		sumNum(k, num);
		return newJsonObjs;
	}

	@Override
	public String getStorePath(K k) {
		return provider.getStorePath(k);
	}

}
