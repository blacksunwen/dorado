/**
 * 
 */
package com.bstek.dorado.data.provider;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-18
 */
public class Order {
	private String property;
	private boolean desc;

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

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}
}
