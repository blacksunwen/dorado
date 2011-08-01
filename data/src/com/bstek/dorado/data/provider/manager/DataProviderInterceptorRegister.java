/**
 * 
 */
package com.bstek.dorado.data.provider.manager;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-8
 */
public class DataProviderInterceptorRegister implements
		BeanFactoryPostProcessor {
	private DataProviderDefinitionManager dataProviderDefinitionManager;
	private MethodInterceptor methodInterceptor;

	public void setDataProviderDefinitionManager(
			DataProviderDefinitionManager dataProviderDefinitionManager) {
		this.dataProviderDefinitionManager = dataProviderDefinitionManager;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (methodInterceptor != null) {
			dataProviderDefinitionManager
					.addDataProviderMethodInterceptor(methodInterceptor);
		}
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

}
