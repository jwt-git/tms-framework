package net.juweitang.tms.common.web.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 
 * @author sam.zhang
 * 
 */
public class MultipartResolver extends CommonsMultipartResolver {

	private static final String ResolvedTag = "_MultipartResolved";

	@Override
	public boolean isMultipart(HttpServletRequest request) {
		if (request.getAttribute(ResolvedTag) != null) {
			return false;
		}
		request.setAttribute(ResolvedTag, ResolvedTag);
		return super.isMultipart(request);
	}

}
