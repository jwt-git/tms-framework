package net.juweitang.tms.common.web.cookyjar;

import java.util.Map;

/**
 * ��http�е�cookie�İ�װ,�õ�����: request.getAttrubute(Cookyjar.CookyjarInRequest)
 * ע��,���keyû�������������ù�,����Ϊ��Ч������ʹ��Filter,����spring��interceptor������ʹ��
 * 
 * @author sam.zhang
 */
public interface Cookyjar {
	public static final String CookyjarInRequest = "cookyjar";

	/**
	 * ����һ��ֵ,����Ѵ���,�򸲸�,���value=null,���൱�� remove(key)
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value);

	public void set(String key, Long value);

	public void set(String key, SelfDependence object);

	public void set(String key, SelfDependence object, int expiry);

	/**
	 * �־û�һ�������л��Ķ���,ע��,ʹ�ô˷���,���صĽ��ǵ�һ�����õĴ�class����
	 * 
	 * @param object
	 */
	public void set(SelfDependence object);

	public void set(SelfDependence object, int expiry);

	/**
	 * ����һ��ֵ,����Ѵ���,�򸲸�,���value=null,���൱�� remove(key)
	 * 
	 * @param key
	 * @param value
	 * @param expiry
	 *            an integer specifying the maximum age of the cookie in
	 *            seconds; if negative, means the cookie is not stored; if zero,
	 *            deletes the cookie
	 */
	public void set(String key, String value, int expiry);

	public void set(String key, Long value, int expiry);

	/**
	 * �õ�һ��ֵ
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * ��cookie�и��key�õ�һ�������л��Ķ���
	 * 
	 * @param key
	 * @return
	 */
	public SelfDependence getObject(String key);

	/**
	 * ��cookie�и����õ�һ�������л��Ķ���
	 * 
	 * @param key
	 * @return
	 */
	public SelfDependence getObject(Class<? extends SelfDependence> objectType);

	/**
	 * ��cookie�и��key�Ͷ�����õ�һ�������л�����
	 * 
	 * @param key
	 * @param objectType
	 * @return
	 */
	public SelfDependence getObject(String key,
			Class<? extends SelfDependence> objectType);

	/**
	 * �õ����е�ֵ��
	 * 
	 * @return
	 */
	public Map<String, String> getAll();

	/**
	 * ɾ��һ��ֵ
	 * 
	 * @param key
	 * @return
	 */
	public String remove(String key);

	public void remove(Class<? extends SelfDependence> objectType);

	/**
	 * 
	 * ������ֵ�����
	 */
	public void clean();
}
