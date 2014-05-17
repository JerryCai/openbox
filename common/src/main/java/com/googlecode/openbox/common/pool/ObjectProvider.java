package com.googlecode.openbox.common.pool;

import java.util.List;

public interface ObjectProvider<K,V> {
	
	V provideObject(K key);
	
	List<V> provideObjects(K key,int num);

}
