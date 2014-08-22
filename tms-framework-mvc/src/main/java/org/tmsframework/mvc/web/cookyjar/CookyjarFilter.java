package org.tmsframework.mvc.web.cookyjar;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.web.filter.GenericFilterBean;

/**
 * cookyjarʹ��ʵ��,ע��,�˹���Ӧ��������ResponseOutputBufferFilter֮��
 * 
 * @author sam.zhang
 */
public class CookyjarFilter extends GenericFilterBean implements Filter {

	private CookyjarConfigure cookyjarConfigure;

	public CookyjarConfigure getCookyjarConfigure() {
		return cookyjarConfigure;
	}

	public void setCookyjarConfigure(CookyjarConfigure cookyjarConfigure) {
		this.cookyjarConfigure = cookyjarConfigure;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Cookyjar ck = (Cookyjar) request
				.getAttribute(Cookyjar.CookyjarInRequest);
		if (ck != null) {
			chain.doFilter(request, response);
			return;
		}
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final CookyjarSimpleImpl jar = new CookyjarSimpleImpl(httpRequest,
				cookyjarConfigure);
		HttpServletResponse httpResponseWrapper = new HttpServletResponseWrapper(
				httpResponse) {
			@Override
			public void sendRedirect(String location) throws IOException {
				jar.commit(httpResponse);
				super.sendRedirect(location);
			}

		};
		initRequest(httpRequest, jar);
		try {
			chain.doFilter(httpRequest, httpResponseWrapper);
		} finally {
			jar.commit(httpResponse);
			cleanRequest(httpRequest, jar);
		}
	}

	private void initRequest(HttpServletRequest httpRequest,
			CookyjarSimpleImpl jar) {
		httpRequest.setAttribute(Cookyjar.CookyjarInRequest, jar);
		for (Iterator<String> it = jar.getCookieNames(); it.hasNext();) {
			String name = it.next();
			Object value = jar.getObject(name);
			if (value == null) {
				value = jar.get(name);
			}
			httpRequest.setAttribute(name, value);
		}
	}

	private void cleanRequest(HttpServletRequest httpRequest,
			CookyjarSimpleImpl jar) {
		httpRequest.removeAttribute(Cookyjar.CookyjarInRequest);
		for (Iterator<String> it = jar.getCookieNames(); it.hasNext();) {
			httpRequest.removeAttribute(it.next());
		}
	}

}
