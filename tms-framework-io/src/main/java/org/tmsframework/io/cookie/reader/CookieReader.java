/**
 * 
 */
package org.tmsframework.io.cookie.reader;

/**
 * @author zhangsen
 *
 */
public interface CookieReader {
	
	public static final String KEY_SPLITER = "----------";

	/**
	 * 读取cookie的内容
	 * @param key	加密算法的密钥
	 * @return
	 */
	public String readAsString(String key);

}
