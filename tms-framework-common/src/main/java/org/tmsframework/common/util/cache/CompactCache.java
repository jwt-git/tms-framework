package org.tmsframework.common.util.cache;

/**
 * cache���ӿ�ʵ��
 * 
 * @author sam.zhang
 */
public interface CompactCache<K,V> {
	/**
	 * ��һ��key - value ����cache
	 * 
	 * @param key
	 *            ����Ϊnull
	 * @param value
	 *            ���Ϊnull�����൱�� remove(key)
	 */
	public void put(K key, V value);

	/**
	 * ��cache�еõ�key��Ӧ��ֵ
	 * 
	 * @param key
	 *            ����Ϊnull
	 * @return ���cache�����ڣ������Ѿ����ڣ��򷵻�null
	 */
	public V get(K key);

	/**
	 * ���cache�е��������
	 * 
	 */
	public void clean();
}
