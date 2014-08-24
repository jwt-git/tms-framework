package org.tmsframework.mvc.web.cookyjar;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;
import org.tmsframework.common.enums.ModelTypeEnum;
import org.tmsframework.mvc.common.Crumbs;

/**
 * @author sam.zhang
 * 
 */
public class CookyjarConfigure implements ServletContextAware,InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(CookyjarConfigure.class);

	private Map<String, CookieConfigure> clientName2CfgMap;

	private Map<String, CookieConfigure> name2CfgMap;

	private Map<Class<? extends SelfSerializable>, CookieConfigure> class2CfgMap;

	private CookieConfigure defaultConfigure;
	
	private ServletContext servletContext;
	
	private List<CookieConfigure> configures;
	
	private String model = ModelTypeEnum.COOKIE.toString();
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public CookieConfigure getConfByName(String name) {
		return this.name2CfgMap.get(name);
	}

	public CookieConfigure getConfByClientName(String clientName) {
		return this.clientName2CfgMap.get(clientName);
	}

	public CookieConfigure getConfByClass(Class<? extends SelfSerializable> clazz) {
		return this.class2CfgMap.get(clazz);
	}

	public void setDefaultConfigure(CookieConfigure defaultConfigure) {
		this.defaultConfigure = defaultConfigure;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setCookieConfigures(List<CookieConfigure> configures) {
		if (configures == null) {
			throw new NullPointerException("configures list can't be null.");
		}
		this.configures = configures;
		if(ModelTypeEnum.SESSION.toString().equalsIgnoreCase(this.model)){
			addCrumbConfigure();
		}
	}
	
	private void addCrumbConfigure() {
		this.configures.add(getStaticCrumbConfigure());
	}

	private CookieConfigure getStaticCrumbConfigure() {
		CookieConfigure configure = new CookieConfigure();
		configure.setClientName("_dd_");
		configure.setName(Crumbs.CRUMBS_CONTENT);
		configure.setHttpOnly(true);
		configure.setSelfSerializableClass(Crumbs.class);
		return configure;
	}

	public void afterPropertiesSet() throws Exception {
		name2CfgMap = new HashMap<String, CookieConfigure>(configures.size());
		clientName2CfgMap = new HashMap<String, CookieConfigure>(configures
				.size());
		class2CfgMap = new HashMap<Class<? extends SelfSerializable>, CookieConfigure>(
				configures.size());
		for (CookieConfigure cfg : configures) {
			buildConf(cfg);
		}
		name2CfgMap = Collections.unmodifiableMap(name2CfgMap);
		clientName2CfgMap = Collections.unmodifiableMap(clientName2CfgMap);
		class2CfgMap = Collections.unmodifiableMap(class2CfgMap);
		if (logger.isDebugEnabled()) {
			logger
					.debug("init name2CfgMap and clientName2CfgMap end.all CookieConfigure:"
							+ name2CfgMap.values());
		}
		
	}

	private void buildConf(CookieConfigure cfg) {
		if (cfg.getName() == null) {
			throw new NullPointerException(
					"CookieConfigure's name can't be null.");
		}
		if (cfg.getClientName() == null) {
			throw new NullPointerException(
					"CookieConfigure's client name can't be null.");
		}
		if (this.defaultConfigure != null) {
			if (cfg.getDomain() == null) {
				cfg.setDomain(this.defaultConfigure.getDomain());
			}
			if (cfg.getEncoding() == null) {
				cfg.setEncoding(this.defaultConfigure.getEncoding());
			}
			if (cfg.getLifeTime() == null) {
				cfg.setLifeTime(this.defaultConfigure.getLifeTime());
			}
			if (cfg.getPath() == null) {
				cfg.setPath(this.defaultConfigure.getPath());
			}
			if (cfg.getRandomChar() == null) {
				cfg.setRandomChar(this.defaultConfigure.getRandomChar());
			}
			if (cfg.getCrypto() == null) {
				cfg.setCrypto(this.defaultConfigure.getCrypto());
			}
		}
		if(this.servletContext.getMajorVersion()<3){
			//httpOnly这个属性在servlet3.0以后才有，所以如果是这个 版本以下容器，覆盖掉如果为true的设置
			cfg.setHttpOnly(false);
		}
		name2CfgMap.put(cfg.getName(), cfg);
		clientName2CfgMap.put(cfg.getClientName(), cfg);
		if (cfg.getSelfSerializableClass() != null) {
			class2CfgMap.put(cfg.getSelfSerializableClass(), cfg);
		}
	}

}