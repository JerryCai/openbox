package com.google.test.openbox.common.pool;

import java.util.List;

public interface ObjectPools<K,V> {
	
	int getMaxSize();
	
	ObjectProvider<K, V> getObjectProvider();
	
	V borrowObject(K key);
	
	List<V> borrowObjects(K key ,int num);
	
	void returnObject(K key ,V value);
	
	void returnObjects(K key ,List<V> values);
	
	int getBusyNum(K key);
	
	int getIdleNum(K key);
	
	int getNum(K key);
	
	int getBusyNum();
	
	int getIdleNum();
	
	int getNum();
	
	int getPoolNum();
	
	String report();
	
	String report(K key);
	
	void clear();
	
	void clear(K key);

}
