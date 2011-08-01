/**
 * 
 */
package com.bstek.dorado.data.provider;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-6-18
 */
public class FilterCriterion implements Criterion {
	String property;
	String expression;

	public FilterCriterion() {
	}

	public FilterCriterion(String property, String expression) {
		this.property = property;
		this.expression = expression;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
