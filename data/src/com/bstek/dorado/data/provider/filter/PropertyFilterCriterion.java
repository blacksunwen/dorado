package com.bstek.dorado.data.provider.filter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-9-18
 */
public class PropertyFilterCriterion extends FilterCriterion {
	private String otherProperty;
	private String otherPropertyPath;

	public String getOtherProperty() {
		return otherProperty;
	}

	public void setOtherProperty(String otherProperty) {
		this.otherProperty = otherProperty;
	}

	public String getOtherPropertyPath() {
		return otherPropertyPath;
	}

	public void setOtherPropertyPath(String otherPropertyPath) {
		this.otherPropertyPath = otherPropertyPath;
	}
}
