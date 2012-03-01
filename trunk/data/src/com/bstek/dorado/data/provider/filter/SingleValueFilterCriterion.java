/**
 * 
 */
package com.bstek.dorado.data.provider.filter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-3-1
 */
public class SingleValueFilterCriterion extends FilterCriterion {
	private FilterOperator filterOperator;
	private Object value;

	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
