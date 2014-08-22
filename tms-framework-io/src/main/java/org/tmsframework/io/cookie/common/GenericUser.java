package org.tmsframework.io.cookie.common;

public class GenericUser{

	
	protected long id = -1L;
	
	protected String name = "";
	
	protected Long eclpLastLoginTime = -1L;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Long getEclpLastLoginTime() {
		return eclpLastLoginTime;
	}

	public void setEclpLastLoginTime(Long eclpLastLoginTime) {
		this.eclpLastLoginTime = eclpLastLoginTime;
	}
	
}
