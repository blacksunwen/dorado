package com.bstek.dorado.data.provider.filter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-3-1
 */
public class SimpleFilterCriterion extends FilterCriterion {
	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
