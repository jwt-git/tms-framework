/**
 * 
 */
package org.tmsframework.io.cookie.reader.impl;

import javax.servlet.http.Cookie;

import org.tmsframework.io.cookie.reader.CookieReader;
import org.tmsframework.util.crypto.Crypto;
import org.tmsframework.util.crypto.impl.AESCryptoImpl;

/**
 * 读取AES算法加密的cookie
 * 
 * @author zhangsen
 *
 */
public class AESCookieReader extends AbstractCookieReader {
	
	public AESCookieReader(Cookie cookie){
		this.cookie = cookie;
	}

	@Override
	public Crypto getCryptoMethod(String key) {
		if(key==null || "".equals(key.trim())){
			return new AESCryptoImpl();
		}else{
			String[] keys = key.split(CookieReader.KEY_SPLITER);
			if(keys[0].length() %16 !=0 || keys[1].length() %16 !=0){
				return new AESCryptoImpl();
			}else{
				AESCryptoImpl aesCrypto = new AESCryptoImpl();
				aesCrypto.setKey(keys[0]);
				aesCrypto.setIvParameter(keys[1]);
				return aesCrypto;
			}
		}
	}

}
