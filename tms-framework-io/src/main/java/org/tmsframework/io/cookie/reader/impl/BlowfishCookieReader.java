/**
 * 
 */
package org.tmsframework.io.cookie.reader.impl;

import javax.servlet.http.Cookie;

import org.tmsframework.util.crypto.Crypto;
import org.tmsframework.util.crypto.impl.BlowfishCryptoImpl;

/**
 * @author zhangsen
 *
 */
public class BlowfishCookieReader extends AbstractCookieReader  {
	
	public BlowfishCookieReader(Cookie cookie){
		this.cookie = cookie;
	}

	@Override
	public Crypto getCryptoMethod(String key) {
		if(key==null || "".equals(key.trim())){
			return new BlowfishCryptoImpl();
		}else{
			BlowfishCryptoImpl blowfishCrypto = new BlowfishCryptoImpl();
			blowfishCrypto.setKey(key.trim());
			return blowfishCrypto;
		}
	}

}
