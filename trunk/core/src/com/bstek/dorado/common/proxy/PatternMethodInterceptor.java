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
package com.bstek.dorado.common.proxy;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-7
 */
public abstract class PatternMethodInterceptor implements MethodInterceptor {
	private String pattern;
	private String excludePattern;
	private int order = 999;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getExcludePattern() {
		return excludePattern;
	}

	public void setExcludePattern(String excludePattern) {
		this.excludePattern = excludePattern;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
