package com.googlecode.openbox.common.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FixSizeObjectPools<K, V> implements ObjectPools<K, V> {
	private static final Logger logger = LogManager.getLogger();

	private static final int INFINIT_MAX_SIZE = Integer.MAX_VALUE;

	private ObjectProvider<K, V> objectProvider;
	private int poolSize;

	private static final int INIT_POOL_NUM = 3;
	private Map<K, BlockingQueue<V>> OBJECT_POOLS = new HashMap<K, BlockingQueue<V>>(
			INIT_POOL_NUM);

	public FixSizeObjectPools(ObjectProvider<K, V> objectProvider, int poolSize) {
		this.objectProvider = objectProvider;
		this.poolSize = poolSize;
	}

	public FixSizeObjectPools(ObjectProvider<K, V> objectProvider) {
		this(objectProvider, INFINIT_MAX_SIZE);
	}

	@Override
	public ObjectProvider<K, V> getObjectProvider() {
		return objectProvider;
	}

	@Override
	public int getMaxSize() {
		return poolSize;
	}

	@Override
	public V borrowObject(K key) {
		initPool(key);
		BlockingQueue<V> pool = OBJECT_POOLS.get(key);
		try {
			return pool.take();
		} catch (InterruptedException e) {
			String message = "borrow object from block queue pool with key=["
					+ key + "] is interrupted !";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	@Override
	public List<V> borrowObjects(K key, int num) {
		List<V> vList = new ArrayList<V>(num);
		for (int i = 0; i < num; i++) {
			vList.add(borrowObject(key));
		}
		return vList;
	}

	@Override
	public void returnObject(K key, V value) {
		synchronized (OBJECT_POOLS) {
			if (!OBJECT_POOLS.containsKey(key)) {
				throw new RuntimeException(
						"you are forbidden to try to return a object=[" + value
								+ "] which not borrow from this pool_[" + key
								+ "]");
			}
			BlockingQueue<V> pool = OBJECT_POOLS.get(key);
			pool.add(value);
			if (logger.isInfoEnabled()) {
				logger.info("return object=[" + value + "] to pool_[" + key
						+ "] success");
			}
		}
	}

	@Override
	public void returnObjects(K key, List<V> values) {
		for (V value : values) {
			returnObject(key, value);
		}
	}

	private void initPool(K key) {
		if (OBJECT_POOLS.containsKey(key)) {
			return;
		}
		synchronized (OBJECT_POOLS) {
			if (OBJECT_POOLS.containsKey(key)) {
				return;
			}
			BlockingQueue<V> pool = new LinkedBlockingQueue<V>();
			List<V> valueList = objectProvider.provideObjects(key, poolSize);
			pool.addAll(valueList);
			OBJECT_POOLS.put(key, pool);
			if (logger.isInfoEnabled()) {
				logger.info("put totally [" + valueList.size()
						+ "] object to pool_[" + key + "] success");
			}
		}
	}

	@Override
	public int getBusyNum(K key) {
		synchronized (OBJECT_POOLS) {
			if (!OBJECT_POOLS.containsKey(key)) {
				return 0;
			}
			BlockingQueue<V> pool = OBJECT_POOLS.get(key);
			return pool.size();
		}
	}

	@Override
	public int getIdleNum(K key) {
		synchronized (OBJECT_POOLS) {
			if (!OBJECT_POOLS.containsKey(key)) {
				return 0;
			}
			BlockingQueue<V> pool = OBJECT_POOLS.get(key);
			return poolSize - pool.size();
		}
	}

	@Override
	public int getNum(K key) {
		synchronized (OBJECT_POOLS) {
			if (!OBJECT_POOLS.containsKey(key)) {
				return 0;
			}
			return poolSize;
		}
	}

	@Override
	public int getBusyNum() {
		synchronized (OBJECT_POOLS) {
			Iterator<K> keys = OBJECT_POOLS.keySet().iterator();
			int num = 0;
			while (keys.hasNext()) {
				K key = keys.next();
				num = num + getBusyNum(key);
			}
			return num;
		}
	}

	@Override
	public int getIdleNum() {
		synchronized (OBJECT_POOLS) {
			Iterator<K> keys = OBJECT_POOLS.keySet().iterator();
			int num = 0;
			while (keys.hasNext()) {
				K key = keys.next();
				num = num + getIdleNum(key);
			}
			return num;
		}
	}

	@Override
	public int getNum() {
		synchronized (OBJECT_POOLS) {
			Iterator<K> keys = OBJECT_POOLS.keySet().iterator();
			int num = 0;
			while (keys.hasNext()) {
				K key = keys.next();
				num = num + getNum(key);
			}
			return num;
		}
	}

	@Override
	public int getPoolNum() {
		synchronized (OBJECT_POOLS) {
			return OBJECT_POOLS.keySet().size();
		}
	}

	@Override
	public void clear() {
		synchronized (OBJECT_POOLS) {
			Iterator<K> keys = OBJECT_POOLS.keySet().iterator();
			while (keys.hasNext()) {
				K key = keys.next();
				clear(key);
			}
			OBJECT_POOLS.clear();
		}
	}

	@Override
	public void clear(K key) {
		synchronized (OBJECT_POOLS) {
			BlockingQueue<V> pool = OBJECT_POOLS.get(key);
			if (null == pool) {
				return;
			}
			pool.clear();
			pool = null;
		}

	}

	@Override
	public String report() {
		StringBuilder sb = new StringBuilder(
				"\n-------------pools status---------------\n");
		sb.append("pool num=[").append(getPoolNum()).append("]")
				.append("total size=[").append(getNum()).append("]")
				.append("total busy num=[").append(getBusyNum()).append("]")
				.append("total idle num=[").append(getIdleNum()).append("]\n");
		sb.append("details for each pool status list below:\n");

		Iterator<K> keys = OBJECT_POOLS.keySet().iterator();
		while (keys.hasNext()) {
			K key = keys.next();
			sb.append(report(key));
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public String report(K key) {
		return "pool key=[" + key + "] ,pool size=[" + getNum(key)
				+ "], busy num=[" + getBusyNum(key) + "],idle num=["
				+ getIdleNum(key) + "]";
	}

	@Override
	public String toString() {
		return report();
	}

}
