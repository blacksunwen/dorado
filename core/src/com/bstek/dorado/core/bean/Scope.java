package com.bstek.dorado.core.bean;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-12-15
 */
public enum Scope {
	/**
	 * 瞬间的，即该实例只在瞬间有效，用完即被抛弃。
	 */
	instant,

	/**
	 * 单例的，即该实例一旦被创建将在整个JVM的运行周期内有效。
	 */
	singleton,

	/**
	 * Web会话范围的生命周期。
	 */
	session,

	/**
	 * Web请求范围的生命周期。
	 */
	request
}
