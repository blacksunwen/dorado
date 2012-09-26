package com.bstek.dorado.data.provider.filter;

import com.bstek.dorado.data.provider.Criterion;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-18
 */
public abstract class FilterCriterion implements Criterion {
	private String property;
	private String propertyPath;
	private FilterOperator filterOperator;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}

	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}

	@Deprecated
	public String getExpression() {
		return null;
	}
}
