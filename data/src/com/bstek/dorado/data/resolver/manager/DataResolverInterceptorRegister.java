/**
 * 
 */
package com.bstek.dorado.data.resolver.manager;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.bstek.dorado.data.config.definition.DataResolverDefinitionManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-9
 */
public class DataResolverInterceptorRegister implements
		BeanFactoryPostProcessor {
	private DataResolverDefinitionManager dataResolverDefinitionManager;
	private MethodInterceptor methodInterceptor;

	public void setDataResolverDefinitionManager(
			DataResolverDefinitionManager dataResolverDefinitionManager) {
		this.dataResolverDefinitionManager = dataResolverDefinitionManager;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (methodInterceptor != null) {
			dataResolverDefinitionManager
					.addDataResolverMethodInterceptor(methodInterceptor);
		}
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

}
