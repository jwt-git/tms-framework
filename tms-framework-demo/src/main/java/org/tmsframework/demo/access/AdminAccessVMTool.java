package org.tmsframework.demo.access;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.tools.view.context.ViewContext;
import org.tmsframework.demo.domain.AdministratorAgent;
import org.tmsframework.demo.enums.FunctionsEnum;
import org.tmsframework.mvc.web.cookyjar.Cookyjar;

/**
 * 
 * @author fish
 * 
 */
public class AdminAccessVMTool {

	private AdministratorAgent agent;

	public void init(Object obj) {
		if (!(obj instanceof ViewContext)) {
			throw new IllegalArgumentException(
					"Tool can only be initialized with a ViewContext");
		}
		ViewContext viewContext = (ViewContext) obj;
		HttpServletRequest request = viewContext.getRequest();
		Cookyjar cookyjar = (Cookyjar) request
				.getAttribute(Cookyjar.CookyjarInRequest);
		if (cookyjar == null) {
			throw new IllegalStateException(
					"Cookyjar not find in HttpServletRequest");
		}
		agent = (AdministratorAgent) cookyjar.getObject(AdministratorAgent.class);
		if (agent == null) {
			throw new IllegalStateException(
					"AdministerAgent not find in Cookyjar");
		}
	}

	public boolean has(String funcationName) {
		FunctionsEnum en = FunctionsEnum.valueOf(funcationName);
		if (en == null) {
			throw new IllegalArgumentException("unknow function name:"
					+ funcationName);
		}
		return this.agent.haveFunction(en);
	}

}
