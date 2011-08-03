package com.bstek.dorado.hibernate.criteria.criterion;


public abstract class SingleProperyCriterion extends BaseCriterion {

	private String propertyName;
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
}
