package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-24
 */
@Widget(name = "CustomSpinner", category = "Form",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.CustomSpinner",
		shortTypeName = "CustomSpinner")
public class CustomSpinner extends Spinner {
	private String value;
	private String pattern;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@IdeProperty(highlight = 1)
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
