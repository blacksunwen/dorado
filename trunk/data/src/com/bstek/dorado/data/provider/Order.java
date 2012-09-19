package com.bstek.dorado.data.provider;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-18
 */
public class Order {
	private String property;
	private String propertyPath;
	private boolean desc;

	public Order() {
	}

	public Order(String property, boolean desc) {
		this.property = property;
		this.desc = desc;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

	@Deprecated
	public String getPropertyPath() {
		return propertyPath;
	}

	@Deprecated
	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}

	public boolean isDesc() {
		return desc;
	}
}
