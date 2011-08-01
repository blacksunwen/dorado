package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;

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

	@ViewAttribute(referenceComponentName = "Trigger", enumValues = "triggerClear,autoMappingDropDown1,autoMappingDropDown2,defaultDateDropDown")
	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
}