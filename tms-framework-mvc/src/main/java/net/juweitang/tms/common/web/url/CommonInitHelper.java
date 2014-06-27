package net.juweitang.tms.common.web.url;

import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author sam.zhang
 * 
 */
public class CommonInitHelper extends URLBrokerInitHelper implements
		InitializingBean {

	private String contextPath = "";

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public void afterPropertiesSet() throws Exception {
		for (URLBroker broker : this.getBrokers()) {
			broker.init(contextPath);
		}
	}

}
