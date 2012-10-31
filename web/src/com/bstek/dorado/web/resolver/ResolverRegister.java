/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.web.resolver;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-13
 */
public class ResolverRegister implements ApplicationContextAware {
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

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		resolverRegisterProcessor.addResolverRegister(this);

	}
}
