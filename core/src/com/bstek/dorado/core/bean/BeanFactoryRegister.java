package com.bstek.dorado.core.bean;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 用于利用外部的Spring配置文件完成Bean工厂注册功能的辅助类。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 26, 2007
 */
public class BeanFactoryRegister implements BeanFactoryPostProcessor {
	private BeanFactoryRegistry beanFactoryRegistry;
	private List<BeanFactory> beanFactories;

	public void setBeanFactoryRegistry(BeanFactoryRegistry beanFactoryRegistry) {
		this.beanFactoryRegistry = beanFactoryRegistry;
	}

	/**
	 * 设置要注册的Bean工厂的集合。
	 */
	public void setBeanFactories(List<BeanFactory> beanFactories) {
		this.beanFactories = beanFactories;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		for (BeanFactory factory : beanFactories) {
			beanFactoryRegistry.registerBeanFactory(factory);
		}
	}
}
