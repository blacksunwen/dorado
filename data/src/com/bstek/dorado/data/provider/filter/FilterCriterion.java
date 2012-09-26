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

	@Deprecated
	private String expression;

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
		return expression;
	}

	@Deprecated
	public void setExpression(String expression) {
		this.expression = expression;
	}
}
