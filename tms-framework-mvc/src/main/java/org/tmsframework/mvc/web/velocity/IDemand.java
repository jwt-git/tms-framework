package org.tmsframework.mvc.web.velocity;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.tools.view.context.ViewContext;
import org.springframework.util.ClassUtils;

public class IDemand {

	private static final String IDemand_In_Request_tag = "_"
			+ ClassUtils.getShortName(IDemand.class) + "_in_request_tag";

	private IDemand parent;

	private Set<String> css;

	private Set<String> js;

	public void init(Object obj) {
		ViewContext viewContext = (ViewContext) obj;
		HttpServletRequest request = viewContext.getRequest();
		IDemand exist = (IDemand) request.getAttribute(IDemand_In_Request_tag);
		if (exist != null) {
			this.parent = exist;
		}
		else{
			request.setAttribute(IDemand_In_Request_tag, this);
		}
	}

	public void addCss(String key) {
		if (parent != null) {
			parent.addCss(key);
			return;
		}
		if (css == null) {
			css = new HashSet<String>();
		}
		css.add(key);
	}

	public boolean haveCss(String key) {
		return isCss(key);
	}

	public boolean isCss(String key) {
		if (parent != null) {
			return parent.isCss(key);
		}
		return css == null ? false : css.contains(key);
	}

	public void addJs(String key) {
		if (parent != null) {
			parent.addJs(key);
			return;
		}
		if (js == null) {
			js = new HashSet<String>();
		}
		js.add(key);
	}

	public boolean haveJs(String key) {
		return isJs(key);
	}

	public boolean isJs(String key) {
		if (parent != null) {
			return parent.isJs(key);
		}
		return js == null ? false : js.contains(key);
	}
}
