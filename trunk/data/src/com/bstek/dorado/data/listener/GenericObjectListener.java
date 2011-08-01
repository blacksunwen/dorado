/**
 * 
 */
package com.bstek.dorado.data.listener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-10
 */
public abstract class GenericObjectListener<T> {
	private int order = 999;
	private String pattern;
	private String excludePattern;

	@SuppressWarnings("rawtypes")
	Class resultType = void.class;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class<T> getParameterizedType() {
		if (resultType == void.class) {
			Class cl = getClass();
			Type superType = cl.getGenericSuperclass();

			if (superType instanceof ParameterizedType) {
				Type[] paramTypes = ((ParameterizedType) superType)
						.getActualTypeArguments();
				if (paramTypes.length > 0) {
					resultType = (Class<T>) paramTypes[0];
				}
			}
		}
		return resultType;
	}

	public abstract boolean beforeInit(T object) throws Exception;

	public abstract void onInit(T object) throws Exception;
}
