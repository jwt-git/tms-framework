package org.tmsframework.util.crypto.impl;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author fish
 * 
 */
public class BlowfishCryptoImpl extends AbstractCryptoImpl {

	private static final String algorithm = "Blowfish";

	private static final String transformation = "Blowfish/ECB/PKCS5Padding";

	private String key = "1Ay6qVwz5ic-=8egkfsdt9f12dalfdz0kHgYuy1X";// default key

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	private static BlowfishCryptoImpl defaultInstance = new BlowfishCryptoImpl();
	
	public static BlowfishCryptoImpl getDefault(){
		return defaultInstance;
	}
	
	@Override
	protected Cipher getDecryptCipher() {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Cipher getEncryptCipher() {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
