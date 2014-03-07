package com.google.test.openbox.jsonpool;

import java.util.List;

public interface JsonStoreProvider<K, V> {

	List<V> get(K dc, int userNum, int fromIndex);

	int getInitSize();

	String getStorePath(K k);
	
	Class<V> getJsonClass();

}
