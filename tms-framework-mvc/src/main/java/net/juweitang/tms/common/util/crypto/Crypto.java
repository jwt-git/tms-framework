package net.juweitang.tms.common.util.crypto;

/**
 * @author sam.zhang
 * 
 */
public interface Crypto {

	public enum Encoding {
		Base64, Base32
	}

	/**
	 * ����string
	 * 
	 * @param s
	 *            ����Ϊnull
	 * @return ���ܺ��string
	 */
	public String encrypt(String s);

	/**
	 * ����string
	 * 
	 * @param s
	 *            ����Ϊnull
	 * @param en
	 *            ���뷽ʽ
	 * @return
	 */
	public String encrypt(String s, Encoding en);

	/**
	 * ����string
	 * 
	 * @param s
	 *            ����Ϊnull
	 * @param encoding
	 *            string�ı����ַ�
	 * @return ���ܺ��string
	 */
	public String encrypt(String s, String charset);

	/**
	 * ����string
	 * 
	 * @param s
	 *            ����Ϊnull
	 * @param en
	 *            ���뷽ʽ
	 * @param charset
	 *            string�ı����ַ�
	 * @return
	 */
	public String encrypt(String s, Encoding en, String charset);

	/**
	 * ��string�����н���
	 * 
	 * @param s
	 *            ����Ϊnull
	 * @return ���ܺ��string
	 */
	public String dectypt(String s);

	/**
	 * ��string�����н���
	 * 
	 * @param s
	 *            ����Ϊnull
	 * @param en
	 *            ���뷽ʽ
	 * @return
	 */
	public String dectypt(String s, Encoding en);

	/**
	 * ��string��ʹ��ָ���ı�����н���
	 * 
	 * @param s
	 *            ����Ϊnull
	 * @param encoding
	 *            �ַ�
	 * @return ���ܺ��string
	 */
	public String dectypt(String s, String encoding);

	/**
	 * ��string�����н���
	 * 
	 * @param s
	 *            ����Ϊnull
	 * @param en
	 *            ����
	 * @param encoding
	 *            �ַ�
	 * @return
	 */
	public String dectypt(String s, Encoding en, String encoding);

	public byte[] encrypt(byte[] bytes);

	public byte[] dectypt(byte[] bytes);

}
