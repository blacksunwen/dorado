package com.bstek.dorado.view.output;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Oct 23, 2008
 */
public abstract class ObjectPropertyOutputter extends ClientObjectOutputter
		implements PropertyOutputter {
	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value);
	}

	public void output(Object object, OutputContext context) throws Exception {
		output(object, context);
	}
}
