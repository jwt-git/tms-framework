package org.tmsframework.mvc.profiler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SpringMVCInterceptor extends HandlerInterceptorAdapter implements
		HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (TimeProfiler.isProfileEnable()) {
			TimeProfiler.beginTask("web action:"+handler.toString());
		}
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (TimeProfiler.isProfileEnable()) {
			TimeProfiler.endTask();
		}
	}

}
