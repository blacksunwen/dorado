/**
 * 
 */
package com.bstek.dorado.data.provider.manager;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.InitializingBean;

import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-8
 */
public class DataProviderInterceptorRegister implements InitializingBean {
	private DataProviderDefinitionManager dataProviderDefinitionManager;
	private MethodInterceptor methodInterceptor;

	public void setDataProviderDefinitionManager(
			DataProviderDefinitionManager dataProviderDefinitionManager) {
		this.dataProviderDefinitionManager = dataProviderDefinitionManager;
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

	public void afterPropertiesSet() throws Exception {
		if (methodInterceptor != null) {
			dataProviderDefinitionManager
					.addDataProviderMethodInterceptor(methodInterceptor);
		}
	}
}
