package com.bstek.dorado.common.service;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-29
 */
public class ExposedService {
	private String name;
	private String beanName;
	private String method;

	public ExposedService(String name, String beanName, String method) {
		this.name = name;
		this.beanName = beanName;
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public String getBeanName() {
		return beanName;
	}

	public String getMethod() {
		return method;
	}
}
