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
