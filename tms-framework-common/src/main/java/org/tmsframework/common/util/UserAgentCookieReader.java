/**
 * 
 */
package org.tmsframework.common.util;

import java.math.BigInteger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.tmsframework.io.cookie.SelfUtil;
import org.tmsframework.io.cookie.common.GenericUser;
import org.tmsframework.io.cookie.reader.CookieReader;
import org.tmsframework.io.cookie.reader.impl.AESCookieReader;
import org.tmsframework.io.cookie.reader.impl.BlowfishCookieReader;
import org.tmsframework.util.crypto.impl.AESCryptoImpl;

/**
 * @author zhangsen
 *
 */
public class UserAgentCookieReader {
	
	/**
	 * 直接获取用AES加密的统一登录平台的用户信息
	 * @param request
	 * @param cookieName
	 * @param randomChar	cookie中的盐长度
	 * @param key	AES算法的key值，业务系统中配置的cookie.aes.key
	 * @param iv	AES算法的iv值，业务系统中配置的cookie.aes.iv
	 * @return
	 */
	public static GenericUser getEclpGenericUserFromAES(HttpServletRequest request,String cookieName,int randomChar,String key,String iv){
		String realCookieValue = getCookieValueFromAES(request,cookieName,randomChar,key,iv);
		return getGenericUser(realCookieValue);
	}
	
	private static GenericUser getGenericUser(String realCookieValue) {
		GenericUser user = new GenericUser();
		String[] values = SelfUtil.recover(realCookieValue);
		if(StringUtils.isNotEmpty(values[0])){
			user.setId(Long.parseLong(values[0]));
		}
		user.setName(values[1]);
		if(StringUtils.isNotEmpty(values[5])){
			user.setEclpLastLoginTime(Long.parseLong(values[5])); 
		}
		return user;
	}

	/**
	 * 直接获取用BlowFish加密的统一登录平台的用户信息
	 * @param request
	 * @param cookieName
	 * @param randomChar
	 * @param key
	 * @return
	 */
	public static GenericUser getEclpGenericUserFromBlowFish(HttpServletRequest request,String cookieName,int randomChar,String key){
		String realCookieValue = getCookieValueFromBlowFish(request, cookieName, randomChar, key);
		return getGenericUser(realCookieValue);
	}
	
	
	/**
	 * 获取用AES算法加密的cookie值
	 * @param request
	 * @param cookieName
	 * @param randomChar	cookie中的盐长度
	 * @param key	AES算法的key值，业务系统中配置的cookie.aes.key
	 * @param iv	AES算法的iv值，业务系统中配置的cookie.aes.iv
	 * @return
	 */
	public static String getCookieValueFromAES(HttpServletRequest request,String cookieName,int randomChar,String key,String iv){
		Cookie cookie = getCookie(request,cookieName);
		if(cookie!=null){
			CookieReader reader = new AESCookieReader(cookie);
			String cookieValue = "";
			if(checkKeyAndIv(key,iv)){
				cookieValue = reader.readAsString(key+CookieReader.KEY_SPLITER+iv);
			}else{
				cookieValue = reader.readAsString(null);
			}
			return cleanSalt(randomChar,cookieValue);
		}
		return "";
	}
	
	private static boolean checkKeyAndIv(String key, String iv) {
		if(key!=null && iv !=null && key.trim().length() %16 == 0 && iv.trim().length() %16 ==0){
			return true;
		}
		return false;
	}

	private static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		for(Cookie item : cookies){
			if(cookieName.equals(item.getName())){
				return item;
			}
		}
		return null;
	}

	/**
	 * 获取用BlowFish算法加密的cookie值
	 * @param request
	 * @param cookieName
	 * @param randomChar	cookie中的盐长度
	 * @param key	密钥
	 * @return
	 */
	public static String getCookieValueFromBlowFish(HttpServletRequest request,String cookieName,int randomChar,String key){
		Cookie cookie = getCookie(request,cookieName);
		if(cookie!=null){
			CookieReader reader = new BlowfishCookieReader(cookie);
			String cookieValue = reader.readAsString(null);
			return cleanSalt(randomChar,cookieValue);
		}
		return "";
	}

	private static String cleanSalt(int randomChar, String contaminatedValue){
		if (randomChar > 0) {
			if (contaminatedValue.length() < randomChar) {
				return null;
			}
			contaminatedValue = contaminatedValue.substring(randomChar);
		}
		return contaminatedValue;
	}
	
	
	public static void main(String[] args) {
		AESCryptoImpl  aesCrypto = new AESCryptoImpl();
		aesCrypto.setKey("t/8p0e3w5AC.q1f2");
		aesCrypto.setIvParameter("4930526051021708");
		String target = "jc3u22m7m3o3m5vjd6xshcreboscxtoxpmcpbze6nrik5zgj65a3uvgnfwags7aawojanv7ifi4li";
		System.out.println("长度："+target.length());
		String v = aesCrypto.dectypt(target);
		System.out.println(v);
		for(String s : SelfUtil.recover(v.substring(4))){
			System.out.println(s);
		}
		
		BigInteger big2 = new BigInteger("17l6o",36);
		System.out.println(big2.toString(36));
		System.out.println(big2.testBit(305011));
	}
}
