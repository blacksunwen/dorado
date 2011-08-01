package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-24
 */
@Widget(name = "CustomSpinner", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.CustomSpinner", shortTypeName = "CustomSpinner")
@XmlNode(nodeName = "CustomSpinner")
public class CustomSpinner extends Spinner {
	private String pattern;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
