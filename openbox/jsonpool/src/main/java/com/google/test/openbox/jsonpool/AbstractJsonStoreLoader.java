package com.google.test.openbox.jsonpool;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AbstractJsonStoreLoader<K, V> implements JsonStoreLoader<K, V> {

	private final Map<K, Boolean> LOAD_INDEX = new HashMap<K, Boolean>(3);

	private final Map<K, Integer> NUM = new HashMap<K, Integer>(3);

	@Override
	public int getCurrentNum(K dc) {
		return NUM.containsKey(dc) ? NUM.get(dc) : 0;
	}

	@Override
	public V load(K dc) {
		return load(dc, 1).get(0);
	}

	@Override
	public synchronized List<V> load(K dc, int num) {
		try {
			if (hasLoaded(dc)) {
				return getJsonObjectsFromProvider(dc, num);
			}
			if (!LOAD_INDEX.containsKey(dc)) {
				LOAD_INDEX.put(dc, Boolean.TRUE);
			}
			if (hasStoreFile(dc)) {
				List<V> list = loadJsonObjectFromStore(dc);
				if (list.size() >= num) {
					num = list.size();
				} else {
					int needIncreaseNum = num - list.size();
					List<V> newJsonObjs = getJsonObjectsFromProvider(dc,
							needIncreaseNum);
					for (V jsonObj : newJsonObjs) {
						list.add(jsonObj);
					}
				}
				return list;
			} else {
				return getJsonObjectsFromProvider(dc, num);
			}
		} catch (Exception e) {
			throw new RuntimeException("load Json object failed !", e);

		}
	}

	protected void sumNum(K dc, int userNum) {
		NUM.put(dc, getCurrentNum(dc) + userNum);
	}

	@Override
	public boolean hasStoreFile(K dc) {
		String filePath = getStorePath(dc);
		File file = new File(filePath);
		return file.exists();
	}

	@Override
	public boolean hasLoaded(K dc) {
		return LOAD_INDEX.containsKey(dc);
	}

	abstract protected List<V> loadJsonObjectFromStore(K dc);

	abstract protected List<V> getJsonObjectsFromProvider(K dc, int userNum);

}
