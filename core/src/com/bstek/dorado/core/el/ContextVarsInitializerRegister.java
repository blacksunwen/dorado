package com.bstek.dorado.core.el;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 用于利用外部的Spring配置文件完成隐式变量初始化器注册功能的辅助类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 17, 2007
 */
public class ContextVarsInitializerRegister implements BeanFactoryPostProcessor {
	private ContextVarsInitializer contextInitializer;

	/**
	 * 设置隐式变量初始化器。
	 */
	public void setContextInitializer(ContextVarsInitializer contextInitializer) {
		this.contextInitializer = contextInitializer;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		DefaultExpressionHandler handler = (DefaultExpressionHandler) beanFactory
				.getBean("dorado.expressionHandler");
		List<ContextVarsInitializer> realInitializers = handler
				.getContextInitializers();
		if (realInitializers != null) {
			realInitializers.add(contextInitializer);
		} else {
			realInitializers = new ArrayList<ContextVarsInitializer>();
			realInitializers.add(contextInitializer);
			handler.setContextInitializers(realInitializers);
		}
	}

}
