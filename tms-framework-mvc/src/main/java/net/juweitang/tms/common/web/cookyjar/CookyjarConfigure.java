package net.juweitang.tms.common.web.cookyjar;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author sam.zhang
 * 
 */
public class CookyjarConfigure {

	private static final Log logger = LogFactory
			.getLog(CookyjarConfigure.class);

	private Map<String, CookieConfigure> clientName2CfgMap;

	private Map<String, CookieConfigure> name2CfgMap;

	private Map<Class<? extends SelfDependence>, CookieConfigure> class2CfgMap;

	private CookieConfigure defaultConfigure;

	public CookieConfigure getConfByName(String name) {
		return this.name2CfgMap.get(name);
	}

	public CookieConfigure getConfByClientName(String clientName) {
		return this.clientName2CfgMap.get(clientName);
	}

	public CookieConfigure getConfByClass(Class<? extends SelfDependence> clazz) {
		return this.class2CfgMap.get(clazz);
	}

	public void setDefaultConfigure(CookieConfigure defaultConfigure) {
		this.defaultConfigure = defaultConfigure;
	}

	public void setCookieConfigures(List<CookieConfigure> configures) {
		if (configures == null) {
			throw new NullPointerException("configures list can't be null.");
		}
		name2CfgMap = new HashMap<String, CookieConfigure>(configures.size());
		clientName2CfgMap = new HashMap<String, CookieConfigure>(configures
				.size());
		class2CfgMap = new HashMap<Class<? extends SelfDependence>, CookieConfigure>(
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
		name2CfgMap.put(cfg.getName(), cfg);
		clientName2CfgMap.put(cfg.getClientName(), cfg);
		if(cfg.getSelfDependenceClass() != null){
			class2CfgMap.put(cfg.getSelfDependenceClass(), cfg);
		}
	}
}
