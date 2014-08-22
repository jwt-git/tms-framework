package org.tmsframework.mvc.web.cookyjar;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.tmsframework.util.crypto.Crypto;

/**
 * @author sam.zhang
 * 
 */
public class CookieConfigure {
	private String name;

	private String clientName;

	private String path;

	private static final Integer DefaultLifeTime = -1;

	private Integer lifeTime = -1;

	// ���Ϊnull,��Ϊ������
	private Crypto crypto;

	// �ַ���룬ȱʡΪutf8
	private String encoding = "UTF-8";

	// ��ֵǰ�����Ӷ���������֣���� <= 0 ,���ʾ������
	private Integer randomChar = 0;

	private String domain;
	
	//domain�Ƿ�����,���domain == "localhost" ���ͬ��δ����
	private boolean isDomainSet = false;

	private Class<? extends SelfDependence> selfDependenceClass;

	// ���ͻ��˵�cookieֵ�������
	public String getRealValue(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		String back = value;
		if (this.crypto != null) {
			back = this.crypto.dectypt(back, Crypto.Encoding.Base32, encoding);
		}
		if (back == null) {
			return null;
		}
		if (this.randomChar > 0) {
			if (back.length() < this.randomChar) {
				return null;
			}
			back = back.substring(this.randomChar);
		}
		return back;

	}

	// ����ʵֵ����ɿͻ���cookie�洢ֵ
	public String getClientValue(String value) {
		if (StringUtils.isBlank(value)) {
			return "";
		}
		String back = value;
		if (back == null) {
			return "";
		}
		if (this.randomChar > 0) {
			back = RandomStringUtils.randomAlphanumeric(this.randomChar) + back;
		}
		if (this.crypto != null) {
			back = this.crypto.encrypt(back, Crypto.Encoding.Base32, encoding);
		}
		return back;
	}

	public Cookie getCookie(String value, String contextPath) {
		return this.getCookie(value, contextPath, this.lifeTime);
	}

	public Cookie getCookie(String value, String contextPath, Integer expiry) {
		Cookie c = new Cookie(getClientName(), getClientValue(value));
		if(this.isDomainSet){
			c.setDomain(domain);
		}
		c.setPath(contextPath + getPath());
		c.setMaxAge(expiry != null ? expiry : DefaultLifeTime);
		return c;
	}

	// �õ�ɾ��һ��cookie��cookie
	public Cookie getDeleteCookie(String contextPath) {
		return this.getCookie("", contextPath, 0);// ������ʱ������Ϊ0��Ϊɾ��һ��cookie
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
		if (!this.path.startsWith("/")) {
			this.path = "/" + this.path;
		}
	}

	/**
	 * @return the clinetName
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * @param clinetName
	 *            the clinetName to set
	 */
	public void setClientName(String clinetName) {
		this.clientName = clinetName;
	}

	public Crypto getCrypto() {
		return crypto;
	}

	public void setCrypto(Crypto crypto) {
		this.crypto = crypto;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the lifeTime
	 */
	public Integer getLifeTime() {
		return lifeTime;
	}

	/**
	 * @param lifeTime
	 *            the lifeTime to set
	 */
	public void setLifeTime(Integer lifeTime) {
		this.lifeTime = lifeTime;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Integer getRandomChar() {
		return randomChar;
	}

	public void setRandomChar(Integer randomChar) {
		this.randomChar = randomChar;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
		if(domain.equals("localhost") || domain.equals(".localhost")){
			this.isDomainSet = false;
		}
		else{
			this.isDomainSet = true;
		}
	}

	public Class<? extends SelfDependence> getSelfDependenceClass() {
		return selfDependenceClass;
	}

	public void setSelfDependenceClass(
			Class<? extends SelfDependence> selfDependenceClass) {
		this.selfDependenceClass = selfDependenceClass;
	}
}
