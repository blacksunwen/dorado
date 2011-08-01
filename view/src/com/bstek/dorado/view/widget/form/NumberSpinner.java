package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-24
 */
@Widget(name = "NumberSpinner", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.NumberSpinner", shortTypeName = "NumberSpinner")
@XmlNode(nodeName = "NumberSpinner")
public class NumberSpinner extends Spinner {
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;

	@ViewAttribute(defaultValue = "-2147483648")
	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	@ViewAttribute(defaultValue = "2147483648")
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
