package com.bstek.dorado.view.output;


/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jun 4, 2009
 */
public class DataPropertyOutputter extends DataOutputter implements
		PropertyOutputter {
	private Object escapeValue = OutputUtils.ESCAPE_VALUE;

	public Object getDefaultValue() {
		return escapeValue;
	}

	public void setEscapeValue(Object escapeValue) {
		this.escapeValue = escapeValue;
	}

	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value, escapeValue);
	}
}
