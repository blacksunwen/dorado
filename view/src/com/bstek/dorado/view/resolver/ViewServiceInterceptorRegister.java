/**
 * 
 */
package com.bstek.dorado.view.resolver;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-7
 */
public class ViewServiceInterceptorRegister implements BeanFactoryPostProcessor {
	private ViewServiceResolver viewServiceResolver;
	private MethodInterceptor methodInterceptor;

	public void setViewServiceResolver(ViewServiceResolver viewServiceResolver) {
		this.viewServiceResolver = viewServiceResolver;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (methodInterceptor != null) {
			viewServiceResolver.addMethodInterceptor(methodInterceptor);
		}
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

}
