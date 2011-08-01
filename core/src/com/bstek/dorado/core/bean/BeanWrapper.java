package com.bstek.dorado.core.bean;

/**
 * Bean的包装器。主要用于通知外界被包装的Bean是否是一个全新的实例。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 13, 2008
 */
public class BeanWrapper {
	private Object bean;
	private boolean newInstance;

	/**
	 * @param bean 被包装的Bean
	 * @param newInstance 是否是一个全新的实例
	 */
	public BeanWrapper(Object bean, boolean newInstance) {
		this.bean = bean;
		this.newInstance = newInstance;
	}

	/**
	 * 返回被包装的Bean。
	 */
	public Object getBean() {
		return bean;
	}

	/**
	 * 返回是否是一个全新的实例。
	 */
	public boolean isNewInstance() {
		return newInstance;
	}
}
