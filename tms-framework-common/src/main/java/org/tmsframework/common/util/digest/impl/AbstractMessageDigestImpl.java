package org.tmsframework.common.util.digest.impl;

import java.io.UnsupportedEncodingException;

import org.tmsframework.common.util.digest.MessageDigest;

/**
 * 信息摘要抽象类
 */
public abstract class AbstractMessageDigestImpl implements MessageDigest {

	// 默认对明文信息转成字节数组采用的编码为UTF-8
	private static final String DEFAULT_CHARSET = "UTF-8";

	// 盐, 默认为null
	private String salt;
	// 字符集编码, 默认为UTF-8
	private String encoding = DEFAULT_CHARSET;

	public void setSalt(String salt) {
		if (salt != null && !"".equals(salt)) {
			this.salt = "{" + salt + "}";
		}
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String digest(String text) {
		return digest(text, this.encoding);
	}

	public String digest(String text, String encoding) {
		if (text == null) {
			return null;
		}
		if (encoding == null) {
			encoding = this.encoding;
		}

		byte[] bytes = null;
		try {
			bytes = mergeMessageAndSalt(text).getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"convert string to byte array failed, unsupport encoding: "
							+ encoding, e);
		}
		return encodeBytesVisibled(digestInternal(bytes));
	}

	/**
	 * 内部原始的信息摘要处理, 由子类实现
	 * 
	 * @param bytes
	 * @return byte[]
	 */
	protected abstract byte[] digestInternal(byte[] bytes);

	/**
	 * 转换信息摘要字节数组为可见字符, 默认采用十六进制表示的字符, 子类可覆盖该方法
	 * 
	 * @param bytes
	 * @return String
	 */
	protected String encodeBytesVisibled(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		byte tb;
		char low;
		char high;
		char tmpChar;

		String hexStr = "";

		for (int i = 0; i < bytes.length; i++) {
			tb = bytes[i];

			tmpChar = (char) ((tb >>> 4) & 0x000f);

			if (tmpChar >= 10) {
				high = (char) (('a' + tmpChar) - 10);
			} else {
				high = (char) ('0' + tmpChar);
			}

			hexStr += high;
			tmpChar = (char) (tb & 0x000f);

			if (tmpChar >= 10) {
				low = (char) (('a' + tmpChar) - 10);
			} else {
				low = (char) ('0' + tmpChar);
			}

			hexStr += low;
		}

		return hexStr;
	}

	/**
	 * 信息加盐处理, 默认为: 明文 {盐}, 子类可以覆盖该方法
	 * 
	 * @param text
	 * @return String
	 */
	protected String mergeMessageAndSalt(String text) {
		if (text == null) {
			text = "";
		}
		if (this.salt == null) {
			return text;
		} else {
			return text + this.salt;
		}
	}

}