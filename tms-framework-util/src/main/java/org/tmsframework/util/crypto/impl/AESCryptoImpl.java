package org.tmsframework.util.crypto.impl;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author sam.zhang
 * 
 */
public class AESCryptoImpl extends AbstractCryptoImpl {

	private static final String algorithm = "AES";

	private static final String transformation = "AES/CBC/PKCS5Padding";

	private String key = "5A82fh-e390V.qw8";

	private String ivParameter = "0102030405060708";

	public void setKey(String key) {
		this.key = key;
	}

	public void setIvParameter(String ivParameter) {
		this.ivParameter = ivParameter;
	}

	public AESCryptoImpl() {
		super();
	}

	private static AESCryptoImpl defaultInstance = new AESCryptoImpl();

	public static AESCryptoImpl getDefault() {
		return defaultInstance;
	}

	@Override
	protected Cipher getDecryptCipher() {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"),
					algorithm);
			Cipher cipher = Cipher.getInstance(transformation);
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Cipher getEncryptCipher() {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"),
					algorithm);
			Cipher cipher = Cipher.getInstance(transformation);
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
