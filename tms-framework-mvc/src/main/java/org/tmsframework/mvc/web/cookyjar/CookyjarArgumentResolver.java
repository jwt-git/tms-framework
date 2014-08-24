package org.tmsframework.mvc.web.cookyjar;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

/**
 * cookyjar解析器
 * 	
 * 		如果方法中带有cookyjar参数，则自动将request中的cookyjar置入
 * 
 * @author sam.zhang
 *
 */
public class CookyjarArgumentResolver implements WebArgumentResolver {
	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		if (methodParameter.getParameterType().equals(Cookyjar.class)) {
			return webRequest.getAttribute(Cookyjar.CookyjarInRequest,
					RequestAttributes.SCOPE_REQUEST);
		}
		return UNRESOLVED;
	}
}
