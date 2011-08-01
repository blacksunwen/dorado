package com.bstek.dorado.core.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 用于配置在Spring文件中，自动初始化{@link com.bstek.dorado.core.bean.BeanFactoryUtils}的辅助类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 26, 2007
 * @see com.bstek.dorado.core.bean.BeanFactoryUtils
 */
public class BeanFactoryUtilsInitializer implements BeanFactoryPostProcessor {
	private BeanFactoryRegistry beanFactoryRegistry;
	private ScopeManager scopeManager;

	/**
	 * 设置Bean工厂的注册管理器。
	 */
	public void setBeanFactoryRegistry(BeanFactoryRegistry beanFactoryRegistry) {
		this.beanFactoryRegistry = beanFactoryRegistry;
	}

	/**
	 * @param scopeManager the scopeManager to set
	 */
	public void setScopeManager(ScopeManager scopeManager) {
		this.scopeManager = scopeManager;
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
			throws BeansException {
		BeanFactoryUtils.setBeanFactoryRegistry(beanFactoryRegistry);
		BeanFactoryUtils.setScopeManager(scopeManager);
	}
}
