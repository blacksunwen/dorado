package com.bstek.dorado.view.manager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-15
 */
public class ViewConfigFactoryRegister implements BeanFactoryPostProcessor,
		Ordered {
	private ViewConfigManager viewConfigManager;
	private String viewNamePattern;
	private Object viewConfigFactory;
	private int order = 999;

	public void setViewConfigManager(ViewConfigManager viewConfigManager) {
		this.viewConfigManager = viewConfigManager;
	}

	public void setViewNamePattern(String viewNamePattern) {
		this.viewNamePattern = viewNamePattern;
	}

	public void setViewConfigFactory(Object viewConfigFactory) {
		this.viewConfigFactory = viewConfigFactory;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Assert.notEmpty(viewNamePattern);
		Assert.notNull(viewConfigFactory);
		viewConfigManager.registerViewConfigFactory(viewNamePattern,
				viewConfigFactory);
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
