package com.bstek.dorado.web;

import org.springframework.beans.factory.BeanFactory;

import com.bstek.dorado.core.bean.SpringBeanFactorySupport;

public class WebSpringBeanFactory extends SpringBeanFactorySupport {

	@Override
	protected BeanFactory getBeanFactory() throws Exception {
		return DoradoContext.getAttachedWebApplicationContext();
	}

	public String getBeanNamePrefix() {
		return "spring";
	}

}
