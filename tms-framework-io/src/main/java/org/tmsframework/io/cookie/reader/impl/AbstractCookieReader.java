/**
 * 
 */
package org.tmsframework.io.cookie.reader.impl;

import javax.servlet.http.Cookie;

import org.tmsframework.io.cookie.reader.CookieReader;
import org.tmsframework.util.crypto.Crypto;

/**
 * @author zhangsen
 *
 */
public abstract class AbstractCookieReader implements CookieReader {
	
	protected Cookie cookie = null;
	
	/* (non-Javadoc)
	 * @see org.tmsframework.io.cookie.reader.CookieReader#readAsString()
	 */
	public String readAsString(String key) {
		if(cookie!=null){
			if(getCryptoMethod(key)==null){//不加密
				return cookie.getValue();
			}
			return getCryptoMethod(key).dectypt(cookie.getValue());
		}else{
			return "";
		}
	}
	
	public abstract Crypto getCryptoMethod(String key);

}
