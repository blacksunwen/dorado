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
/**
 * 
 */
package com.bstek.dorado.view.resolver;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-7
 */
public class ViewServiceInterceptorRegister implements InitializingBean {
	private ViewServiceResolver viewServiceResolver;
	private MethodInterceptor methodInterceptor;

	public void setViewServiceResolver(ViewServiceResolver viewServiceResolver) {
		this.viewServiceResolver = viewServiceResolver;
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

	public void afterPropertiesSet() throws Exception {
		if (methodInterceptor != null) {
			viewServiceResolver.addMethodInterceptor(methodInterceptor);
		}
	}
}
