package com.bstek.dorado.common;

/**
 * 可获知父对象的对象的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Dec 30, 2007
 */
public interface ParentAware<T> {
	/**
	 * 设置父对象
	 */
	void setParent(T parent);

	T getParent();
}
