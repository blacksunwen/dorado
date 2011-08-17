/**
 * 
 */
package com.bstek.dorado.view.resolver;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-7
 */
public class ViewServiceInterceptorRegister implements InitializingBean {
	private ViewServiceResolver viewServiceResolver;
	private MethodInterceptor methodInterceptor;

	public void setViewServiceResolver(ViewServiceResolver viewServiceResolver) {
		this.viewServiceResolver = viewServiceResolver;
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

	public void afterPropertiesSet() throws Exception {
		if (methodInterceptor != null) {
			viewServiceResolver.addMethodInterceptor(methodInterceptor);
		}
	}
}
