package com.googlecode.openbox.jsonpool;

import java.util.List;

public interface JsonStorePool<K,V> {

	V get(K k);

	List<V> get(K k, int num);

	void back(K k, V v);

	void back(K k, List<V> vs);

}
