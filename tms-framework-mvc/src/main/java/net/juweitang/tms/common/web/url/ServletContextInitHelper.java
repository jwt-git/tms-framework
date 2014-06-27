package net.juweitang.tms.common.web.url;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

/**
 * 
 * @author sam.zhang
 * 
 */
public class ServletContextInitHelper extends URLBrokerInitHelper implements
		ServletContextAware,InitializingBean {

	private ServletContext servletContext;
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void afterPropertiesSet() throws Exception {
		try {
			Method getContextPath = ServletContext.class.getDeclaredMethod(
					"getContextPath", new Class[] {});
			if (getContextPath != null) {
				String contextPath = (String) getContextPath.invoke(
						servletContext, new Object[] {});
				if (logger.isDebugEnabled()) {
					logger
							.debug("Servlet 2.5 implement find.so init context path:"
									+ contextPath);
				}
				for (URLBroker broker : this.getBrokers()) {
					broker.init(contextPath);
				}
			}
		} catch (Exception e) {
			logger.error("error in init servlet context path", e);
		}
		
	}
}
