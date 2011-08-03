package com.bstek.dorado.hibernate.criteria.projection;

public abstract class SinglePropertyProjection extends BaseProjection {
	private String propertyName;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
