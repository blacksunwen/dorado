package com.bstek.dorado.web.resolver;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-13
 */
public class ResolverRegister implements BeanFactoryPostProcessor {
	private ResolverRegisterProcessor resolverRegisterProcessor;

	private String url;
	private Object resolver;
	private int order = 999;

	public void setResolverRegisterProcessor(
			ResolverRegisterProcessor resolverRegisterProcessor) {
		this.resolverRegisterProcessor = resolverRegisterProcessor;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getResolver() {
		return resolver;
	}

	public void setResolver(Object resolver) {
		this.resolver = resolver;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		resolverRegisterProcessor.addResolverRegister(this);
	}
}
