package org.tmsframework.common.util.cache.local;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tmsframework.common.util.cache.CompactCache;

/**
 * �򵥵�ʹ��jvm��hashMap ʵ��cache
 * 
 * @author sam.zhang
 * 
 */
public class SimpleHashMapCompactCache<K, V> implements CompactCache<K, V> {

	private Map<K, V> map = new ConcurrentHashMap<K, V>();

	public void clean() {
		this.map.clear();
	}

	public V get(K key) {
		return map.get(key);
	}

	public void put(K key, V value) {
		if (value == null) {
			map.remove(key);
		} else {
			map.put(key, value);
		}

	}

}
