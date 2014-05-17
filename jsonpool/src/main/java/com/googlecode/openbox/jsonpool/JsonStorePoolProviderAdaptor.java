package com.googlecode.openbox.jsonpool;

import java.util.List;

import com.googlecode.openbox.common.pool.ObjectProvider;

class JsonStorePoolProviderAdaptor<K, V> implements ObjectProvider<K, V> {
	private JsonStoreLoader<K, V> loader;

	public JsonStorePoolProviderAdaptor(JsonStoreProvider<K, V> provider) {
		this.loader = new DefaultJsonStoreLoader<K, V>(provider,
				provider.getJsonClass());
	}

	@Override
	public V provideObject(K k) {
		return loader.load(k);
	}

	@Override
	public List<V> provideObjects(K k, int num) {
		return loader.load(k, num);
	}

}
