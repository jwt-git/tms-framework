package org.tmsframework.demo.domain;

import java.io.Serializable;

/**
 * 
 * @author fish
 * 
 */
public class Administer implements Serializable {

	private static final long serialVersionUID = -4924645412681855140L;

	private String id;

	private String loginId;

	private String password;

	private String realName;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}
