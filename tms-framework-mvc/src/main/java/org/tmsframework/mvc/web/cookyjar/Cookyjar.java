package org.tmsframework.mvc.web.cookyjar;

import java.util.Map;

/**
 * 对http中的cookie的包装,得到方法: request.getAttrubute(Cookyjar.CookyjarInRequest)
 * 注意,如果key没有在配置中设置过,则视为无效。可以使用Filter,或者spring的interceptor来配置使用
 * 
 * @author zhangsen
 */
public interface Cookyjar {
	public static final String CookyjarInRequest = "cookyjar";

	/**
	 * 设置一个值,如果已存在,则覆盖,如果value=null,则相当于 remove(key)
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value);

	public void set(String key, Long value);

	public void set(String key, SelfSerializable object);

	public void set(String key, SelfSerializable object, int expiry);

	/**
	 * 持久化一个可序列化的对象,注意,使用此方法,返回的将是第一个配置的此class对象
	 * 
	 * @param object
	 */
	public void set(SelfSerializable object);

	public void set(SelfSerializable object, int expiry);

	/**
	 * 设置一个值,如果已存在,则覆盖,如果value=null,则相当于 remove(key)
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
	 * 得到一个值
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * 从cookie中根据key得到一个反序列化的对象
	 * 
	 * @param key
	 * @return
	 */
	public SelfSerializable getObject(String key);

	/**
	 * 从cookie中根据类得到一个反序列化的对象
	 * 
	 * @param key
	 * @return
	 */
	public SelfSerializable getObject(Class<? extends SelfSerializable> objectType);

	/**
	 * 从cookie中根据key和对象类得到一个反序列化对象
	 * 
	 * @param key
	 * @param objectType
	 * @return
	 */
	public SelfSerializable getObject(String key,
			Class<? extends SelfSerializable> objectType);

	/**
	 * 得到所有的值对
	 * 
	 * @return
	 */
	public Map<String, String> getAll();

	/**
	 * 删除一个值
	 * 
	 * @param key
	 * @return
	 */
	public String remove(String key);

	public void remove(Class<? extends SelfSerializable> objectType);

	/**
	 * 
	 * 将所有值都清除
	 */
	public void clean();
}
