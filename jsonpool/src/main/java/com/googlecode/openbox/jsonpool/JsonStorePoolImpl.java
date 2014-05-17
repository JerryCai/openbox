package com.googlecode.openbox.jsonpool;

import java.util.List;

import com.googlecode.openbox.common.pool.InitSizeObjectPools;
import com.googlecode.openbox.common.pool.ObjectPools;

public class JsonStorePoolImpl<K, V> implements JsonStorePool<K, V> {

	private ObjectPools<K, V> pools;

	public JsonStorePoolImpl(JsonStoreProvider<K, V> vProvider) {
		this.pools = new InitSizeObjectPools<K, V>(
				new JsonStorePoolProviderAdaptor<K, V>(vProvider),
				vProvider.getInitSize());
	}

	@Override
	public V get(K k) {
		return pools.borrowObject(k);
	}

	@Override
	public List<V> get(K k, int num) {
		return pools.borrowObjects(k, num);
	}

	@Override
	public void back(K k, V v) {

		pools.returnObject(k, v);
	}

	@Override
	public void back(K k, List<V> vs) {
		for (V v : vs) {
			back(k, v);
		}
	}
}
