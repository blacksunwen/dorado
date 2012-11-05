/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
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

	@Override
	@IdeProperty(visible = false)
	public String getText() {
		return super.getText();
	}

	@ClientProperty(
			outputter = "class:com.bstek.dorado.view.widget.form.CustomSpinnerValueOutputter")
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
