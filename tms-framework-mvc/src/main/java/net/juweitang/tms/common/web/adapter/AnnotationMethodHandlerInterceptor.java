package net.juweitang.tms.common.web.adapter;

import java.lang.reflect.Method;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author sam.zhang
 * @version $Id: AnnotationMethodHandlerInterceptor.java,v 1.1 2009/11/30 02:33:47 sam.zhang Exp $
 */
public interface AnnotationMethodHandlerInterceptor {
	public void preInvoke(Method handlerMethod, Object handler,
			ServletWebRequest webRequest);

	public void postInvoke(Method handlerMethod, Object handler,
			ServletWebRequest webRequest, ModelAndView mav);
}
