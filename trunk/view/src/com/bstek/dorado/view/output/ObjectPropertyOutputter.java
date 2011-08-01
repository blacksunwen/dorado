package com.bstek.dorado.view.output;


/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 23, 2008
 */
public class ObjectPropertyOutputter extends ViewObjectOutputter implements
		PropertyOutputter {
	private Object escapeValue = OutputUtils.ESCAPE_VALUE;

	public Object getEscapeValue() {
		return escapeValue;
	}

	public void setEscapeValue(Object escapeValue) {
		this.escapeValue = escapeValue;
	}

	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value, escapeValue);
	}
}
