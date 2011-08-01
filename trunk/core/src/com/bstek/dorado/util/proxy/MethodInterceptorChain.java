/**
 * 
 */
package com.bstek.dorado.util.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-7
 */
public class MethodInterceptorChain {
	private MethodInterceptor[] subMethodInterceptors;
	private int index = 0;
	private MethodInterceptor finalMethodInterceptor;
	private MethodInterceptorFilter filter;

	public MethodInterceptorChain(MethodInterceptor[] subMethodInterceptors,
			MethodInterceptor finalMethodInterceptor,
			MethodInterceptorFilter filter) {
		this.subMethodInterceptors = subMethodInterceptors;
		this.finalMethodInterceptor = finalMethodInterceptor;
		this.filter = filter;
	}

	public MethodInterceptor next(MethodInvocation methodInvocation) {
		MethodInterceptor methodInterceptor = null;
		while (index < subMethodInterceptors.length) {
			methodInterceptor = subMethodInterceptors[index++];
			if (filter != null
					&& !filter.filter(methodInterceptor, methodInvocation)) {
				methodInterceptor = null;
			}
			if (methodInterceptor != null) {
				break;
			}
		}
		if (methodInterceptor == null) {
			methodInterceptor = finalMethodInterceptor;
		}
		return methodInterceptor;
	}
}
