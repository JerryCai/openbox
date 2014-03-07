package com.google.test.openbox.jsonpool;

import java.util.List;


interface JsonStoreLoader<K,V> {
	
	boolean hasStoreFile(K k);
	
	boolean hasLoaded(K k);
	
	String getStorePath(K k);
	
	V load(K k);
	
	List<V> load(K k ,int num);
	
	int getCurrentNum(K k);
}
