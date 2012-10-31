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

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.ComponentReference;

@ClientEvents({ @ClientEvent(name = "onTextEdit"),
		@ClientEvent(name = "onTriggerClick"),
		@ClientEvent(name = "onValidationStateChange") })
public abstract class AbstractTextBox extends AbstractDataEditor {
	private String text;
	private String trigger;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@ComponentReference("Trigger")
	@IdeProperty(
			enumValues = "triggerClear,autoMappingDropDown1,autoMappingDropDown2,defaultDateDropDown,defaultDateTimeDropDown,defaultYearMonthDropDown")
	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
}
