/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.widget.form;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-5
 */
@Widget(name = "RadioGroup", category = "Form", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.RadioGroup",
		shortTypeName = "RadioGroup")
@ClientEvents({ @ClientEvent(name = "onValueChange") })
public class RadioGroup extends AbstractDataEditor {
	private Object value;
	private RadioGroupLayout layout = RadioGroupLayout.flow;
	private List<RadioButton> radioButtons = new ArrayList<RadioButton>();

	@XmlProperty
	@ClientProperty
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@ClientProperty(escapeValue = "flow")
	public RadioGroupLayout getLayout() {
		return layout;
	}

	public void setLayout(RadioGroupLayout layout) {
		this.layout = layout;
	}

	public void addRadioButton(RadioButton radioButton) {
		radioButtons.add(radioButton);
	}

	@XmlSubNode
	@ClientProperty
	public List<RadioButton> getRadioButtons() {
		return radioButtons;
	}
}
