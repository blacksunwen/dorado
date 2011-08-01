package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.view.output.OutputUtils;
import com.bstek.dorado.view.output.PropertyOutputter;
import com.bstek.dorado.view.output.ViewObjectOutputter;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-28
 */
public class DefaultLayoutConstraintPropertyOutputter extends
		ViewObjectOutputter implements PropertyOutputter {
	public boolean isEscapeValue(Object value) {
		return OutputUtils.isEscapeValue(value);
	}
}
