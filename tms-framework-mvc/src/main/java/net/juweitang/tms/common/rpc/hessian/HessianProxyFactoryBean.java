package net.juweitang.tms.common.rpc.hessian;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * {@link FactoryBean} for Hessian proxies. Exposes the proxied service for use
 * as a bean reference, using the specified service interface.
 * 
 * <p>
 * Hessian is a slim, binary RPC protocol. For information on Hessian, see the
 * <a href="http://www.caucho.com/hessian">Hessian website</a>
 * 
 * <p>
 * The service URL must be an HTTP URL exposing a Hessian service. For details,
 * see the {@link HessianClientInterceptor} javadoc.
 * 
 * @author Juergen Hoeller
 * @since 13.05.2003
 * @see #setServiceInterface
 * @see #setServiceUrl
 * @see HessianClientInterceptor
 * @see HessianServiceExporter
 * @see org.springframework.remoting.caucho.BurlapProxyFactoryBean
 * @see org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean
 * @see org.springframework.remoting.rmi.RmiProxyFactoryBean
 */
public class HessianProxyFactoryBean extends HessianClientInterceptor implements
		FactoryBean<Object> {

	private Object serviceProxy;

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.serviceProxy = new ProxyFactory(getServiceInterface(), this)
				.getProxy(getBeanClassLoader());
	}

	public Object getObject() {
		return this.serviceProxy;
	}

	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	public boolean isSingleton() {
		return true;
	}

}
