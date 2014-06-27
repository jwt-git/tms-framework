package net.juweitang.tms.common.profiler;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ClassUtils;

/**
 * 对方法的执行时间进行跟踪
 * 
 */
public class MethodProfileInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation invoction) throws Throwable {
		if (TimeProfiler.isProfileEnable()) {
			return invokeWithProfile(invoction);
		} else {
			return invoction.proceed();
		}

	}

	private Object invokeWithProfile(MethodInvocation invoction)
			throws Throwable {
		Method method = invoction.getMethod();
		Class clazz = method.getDeclaringClass();
		String className = ClassUtils.getShortClassName(clazz);
		TimeProfiler.beginTask(new StringBuilder(className).append(':').append(
				method.getName()).toString());
		Object ret;
		try {
			ret = invoction.proceed();
		} finally {
			TimeProfiler.endTask();
		}
		return ret;
	}
}