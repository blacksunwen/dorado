package com.bstek.dorado.data.provider.filter;

import com.bstek.dorado.data.provider.Criterion;
import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-18
 */
public abstract class FilterCriterion implements Criterion {
	String property;
	DataType dataType;
	String expression;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
