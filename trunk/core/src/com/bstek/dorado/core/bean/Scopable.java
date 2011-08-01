package com.bstek.dorado.core.bean;


/**
 * 支持作用范围定义的对象的通用接口。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 29, 2008
 */
public interface Scopable {
	/**
	 * 设置作用范围。
	 */
	void setScope(Scope scope);
}
