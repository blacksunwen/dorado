package com.bstek.dorado.view.output;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-4
 */
public class PropertyConfig {
	public static final Object NONE_VALUE = new Object();

	private boolean ignored;
	private Object escapeValue = NONE_VALUE;
	private Object outputter;
	private boolean evaluateExpression = true;

	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public Object getEscapeValue() {
		return escapeValue;
	}

	public void setEscapeValue(Object escapeValue) {
		this.escapeValue = escapeValue;
	}

	public Object getOutputter() {
		return outputter;
	}

	public void setOutputter(Object outputter) {
		this.outputter = outputter;
	}

	public boolean isEvaluateExpression() {
		return evaluateExpression;
	}

	public void setEvaluateExpression(boolean evaluateExpression) {
		this.evaluateExpression = evaluateExpression;
	}
}
