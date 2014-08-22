package org.tmsframework.mvc.web.url;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author sam.zhang
 *
 */
public class URLBrokerLauncherInterceptor extends HandlerInterceptorAdapter {
	private static String myTag = "_"
			+ URLBrokerLauncherInterceptor.class.getName();

	private static String tagValue = "1";

	private Map<String, URLBroker> brokers;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Object tag = request.getAttribute(myTag);
		if (tag == null) {
			for (Entry<String, URLBroker> entry : brokers.entrySet()) {
				request.setAttribute(entry.getKey(), entry.getValue());
			}
			request.setAttribute(myTag, tagValue);
		}

		return super.preHandle(request, response, handler);
	}

	public Map<String, URLBroker> getBrokers() {
		return brokers;
	}

	public void setBrokers(Map<String, URLBroker> brokers) {
		this.brokers = brokers;
	}

}
