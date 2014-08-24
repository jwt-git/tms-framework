package org.tmsframework.demo.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.tmsframework.demo.domain.AdministratorAgent;
import org.tmsframework.mvc.web.cookyjar.Cookyjar;

/**
 * 
 * @author fish
 * 
 */
public class AdministratorAgentArgumentResolver implements WebArgumentResolver {

	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		if (methodParameter.getParameterType().equals(AdministratorAgent.class)) {
			Cookyjar cookyjar = (Cookyjar) webRequest
					.getAttribute(Cookyjar.CookyjarInRequest,
							RequestAttributes.SCOPE_REQUEST);
			if (cookyjar != null) {
				return cookyjar.getObject(AdministratorAgent.class);
			}
		}
		return UNRESOLVED;
	}
}
