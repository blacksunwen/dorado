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
public interface MethodInterceptorFilter {
	public boolean filter(MethodInterceptor methodInterceptor,
			MethodInvocation methodInvocation);
}
