package net.juweitang.tms.common.web.cookyjar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author sam.zhang
 * 
 */
public class CookyjarSimpleImpl implements Cookyjar {

	private static final Log logger = LogFactory
			.getLog(CookyjarSimpleImpl.class);
	// name -> cookieValue
	private Map<String, CookieValue> cookieMap = new HashMap<String, CookieValue>();

	private CookyjarConfigure cookyjarConfigure;

	private HttpServletRequest httpRequest;

	public CookyjarSimpleImpl(HttpServletRequest httpRequest,
			CookyjarConfigure cookyjarConfigure) {
		this.httpRequest = httpRequest;
		this.cookyjarConfigure = cookyjarConfigure;
		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie ck : cookies) {
				String name = ck.getName();
				CookieConfigure cfg = this.cookyjarConfigure
						.getConfByClientName(name);
				if (cfg == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("get a unknow cookie name[" + name
								+ "]value[" + ck.getValue() + "]");
					}
					continue;
				}
				CookieValue cv = new CookieValue();
				cv.cfg = cfg;
				cv.encrypted = ck.getValue();
				cookieMap.put(cfg.getName(), cv);
			}
		}
	}

	public void commit(HttpServletResponse httpResponse) {
		if (!valueModified()) {
			// ֵδ�޸Ĺ�,����Ҫ����
			if (logger.isDebugEnabled()) {
				logger.debug("cookyjar not modified.");
			}
			return;
		}
		if (httpResponse.isCommitted()) {
			logger
					.error("HttpServletResponse is commited,so cookie can't write to reponse.");
			return;
		}
		String contextPath = httpRequest.getContextPath();
		for (CookieValue cv : cookieMap.values()) {
			if (cv.modified) {
				Cookie cookie = null;
				if (cv.unencrypt == null) {
					cookie = cv.cfg.getDeleteCookie(contextPath);
				} else {
					cookie = cv.cfg.getCookie(cv.unencrypt, contextPath,
							cv.expiredTime);
				}
				cv.modified = false;
				if (logger.isDebugEnabled()) {
					logger.debug("add a cookie:[" + getCookieString(cookie)
							+ "] to http response.");
				}
				httpResponse.addCookie(cookie);
			}
		}
	}

	private boolean valueModified() {
		for (CookieValue cv : cookieMap.values()) {
			if (cv.modified) {
				return true;
			}
		}
		return false;
	}

	public void clean() {
		for (CookieValue cv : cookieMap.values()) {
			cv.unencrypt = null;
			cv.modified = true;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("remove all values ");
		}
	}

	public String get(String key) {
		if (key == null) {
			throw new NullPointerException("key can't be null.");
		}
		CookieValue cv = cookieMap.get(key);
		if (cv == null) {
			return null;
		}
		if (cv.modified) {
			return cv.unencrypt;
		}
		if (cv.unencrypt == null) {
			cv.unencrypt = cv.cfg.getRealValue(cv.encrypted);
		}
		return cv.unencrypt;
	}

	public Map<String, String> getAll() {
		if (cookieMap.isEmpty()) {
			return new HashMap<String, String>();
		}
		Map<String, String> back = new HashMap<String, String>(cookieMap.size());
		for (String name : cookieMap.keySet()) {
			String value = get(name);
			back.put(name, value);
		}
		return back;
	}

	public String remove(String key) {
		if (key == null) {
			throw new NullPointerException("key can't be null.");
		}
		String value = get(key);
		CookieValue cv = cookieMap.get(key);
		if (cv == null) {
			return null;
		}
		cv.unencrypt = null;
		cv.modified = true;
		if (logger.isDebugEnabled()) {
			logger.debug("remove value with key:" + key);
		}
		return value;
	}

	public void remove(Class<? extends SelfDependence> objectType) {
		CookieConfigure conf = this.cookyjarConfigure
				.getConfByClass(objectType);
		if (conf == null) {
			throw new IllegalArgumentException("unknow object class:"
					+ objectType);
		}
		this.remove(conf.getName());
	}

	private void setValue(String key, Object value, Integer expiry) {
		if (value == null) {
			remove(key);
			return;
		}
		if (key == null) {
			throw new NullPointerException("key can't be null.");
		}
		CookieConfigure cfg = this.cookyjarConfigure.getConfByName(key);
		if (cfg == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("set cookie name[" + key + "] value[" + value
						+ "],but this name not configured.");
			}
			return;
		}
		CookieValue cv = new CookieValue();
		cv.cfg = cfg;
		cv.unencrypt = value.toString();
		cv.expiredTime = expiry == null ? cfg.getLifeTime() : expiry;
		cv.modified = true;
		cookieMap.put(key, cv);
	}

	public void set(String key, String value) {
		setValue(key, value, null);
	}

	public void set(String key, Long value) {
		setValue(key, value, null);
	}

	public void set(String key, String value, int expiry) {
		setValue(key, value, expiry);
	}

	public void set(String key, Long value, int expiry) {
		setValue(key, value, expiry);
	}

	public SelfDependence getObject(String key,
			Class<? extends SelfDependence> objectType) {
		String value = this.get(key);
		if (value == null) {
			return null;
		}
		try {
			SelfDependence object = objectType.newInstance();
			object.riseUp(value);
			return object;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public SelfDependence getObject(String key) {
		CookieConfigure conf = this.cookyjarConfigure.getConfByName(key);
		if (conf == null) {
			throw new IllegalArgumentException("unknow cookie key:" + key);
		}
		Class<? extends SelfDependence> clazz = conf.getSelfDependenceClass();
		if (clazz == null) {
//			throw new IllegalArgumentException("cookie with key:" + key
//					+ " not configured with SelfDependenceClass class");
			return null;
		}
		return getObject(key, clazz);
	}

	public SelfDependence getObject(Class<? extends SelfDependence> objectType) {
		CookieConfigure conf = this.cookyjarConfigure
				.getConfByClass(objectType);
		if (conf == null) {
			throw new IllegalArgumentException("unknow object class:"
					+ objectType);
		}
		return getObject(conf.getName(), objectType);
	}

	public void set(String key, SelfDependence object) {
		if (object == null) {
			setValue(key, null, null);
		} else {
			setValue(key, object.lieDown(), null);
		}
	}

	public void set(String key, SelfDependence object, int expiry) {
		if (object == null) {
			setValue(key, null, null);
		} else {
			setValue(key, object.lieDown(), expiry);
		}
	}

	public void set(SelfDependence object) {
		CookieConfigure conf = this.cookyjarConfigure.getConfByClass(object
				.getClass());
		if (conf == null) {
			throw new IllegalArgumentException("unknow object class:"
					+ object.getClass());
		}
		this.set(conf.getName(), object);
	}

	public void set(SelfDependence object, int expiry) {
		CookieConfigure conf = this.cookyjarConfigure.getConfByClass(object
				.getClass());
		if (conf == null) {
			throw new IllegalArgumentException("unknow object class:"
					+ object.getClass());
		}
		this.set(conf.getName(), object, expiry);
	}

	public Iterator<String> getCookieNames() {
		return this.cookieMap.keySet().iterator();
	}

	static final class CookieValue {
		private CookieConfigure cfg;

		private String unencrypt;

		private String encrypted;

		private Integer expiredTime;

		private boolean modified = false;
	}

	private final String getCookieString(Cookie c) {
		if (c == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer(c.toString());
		sb.append(" name[").append(c.getName()).append("] value[").append(
				c.getValue()).append("] domain[");
		sb.append(c.getDomain()).append("] path[").append(c.getPath()).append(
				"] maxAge[").append(c.getMaxAge());
		sb.append("]");
		return sb.toString();
	}

}
