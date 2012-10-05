/**
 *
 */
package com.bstek.dorado.data.method;

import java.lang.reflect.Method;

/**
 * 方法描述对象。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 11, 2008
 */
public class MethodDescriptor {
	private Method method;
	private int[] argIndexs;
	private int matchingRate;

	public MethodDescriptor(Method method, int[] argIndexs, int matchingRate) {
		this.method = method;
		this.argIndexs = argIndexs;
		this.matchingRate = matchingRate;
	}

	public Method getMethod() {
		return method;
	}

	public int[] getArgIndexs() {
		return argIndexs;
	}

	public int getMatchingRate() {
		return matchingRate;
	}
}
