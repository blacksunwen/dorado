package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-5
 */
@Widget(name = "CheckBox", category = "Form", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.CheckBox", shortTypeName = "CheckBox")
@ClientEvents({ @ClientEvent(name = "onValueChange") })
public class CheckBox extends AbstractDataEditor {
	private Object onValue = true;
	private Object offValue = false;
	private Object mixedValue;
	private String caption;
	private Object value = false;
	private Boolean checked;
	private boolean triState;

	@XmlProperty
	@ClientProperty(escapeValue = "true")
	public Object getOnValue() {
		return onValue;
	}

	public void setOnValue(Object onValue) {
		this.onValue = onValue;
	}

	@XmlProperty
	@ClientProperty(escapeValue = "false")
	public Object getOffValue() {
		return offValue;
	}

	public void setOffValue(Object offValue) {
		this.offValue = offValue;
	}

	@XmlProperty
	@ClientProperty
	public Object getMixedValue() {
		return mixedValue;
	}

	public void setMixedValue(Object mixedValue) {
		this.mixedValue = mixedValue;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@XmlProperty
	@ClientProperty(escapeValue = "false")
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Boolean isChecked() {
		return checked;
	}

	public Boolean getChecked() {
		return checked;
	}

	public boolean isTriState() {
		return triState;
	}

	public void setTriState(boolean triState) {
		this.triState = triState;
	}

}
