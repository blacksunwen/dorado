package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "TextArea", category = "Form", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.TextArea", shortTypeName = "TextArea")
public class TextArea extends AbstractTextEditor {
	private boolean required;
	private int minLength;
	private int maxLength;

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
