package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-5
 */
@Widget(name = "CheckBox", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.CheckBox", shortTypeName = "CheckBox")
@XmlNode(nodeName = "CheckBox")
@ClientEvents({ @ClientEvent(name = "onValueChange") })
public class CheckBox extends AbstractDataEditor {
	private Object onValue = true;
	private Object offValue = false;
	private String caption;
	private boolean showCaption = true;
	private Object value = false;
	private Boolean checked;
	private boolean triState;

	@ViewAttribute(defaultValue = "true")
	public Object getOnValue() {
		return onValue;
	}

	public void setOnValue(Object onValue) {
		this.onValue = onValue;
	}

	@ViewAttribute(defaultValue = "false")
	public Object getOffValue() {
		return offValue;
	}

	public void setOffValue(Object offValue) {
		this.offValue = offValue;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isShowCaption() {
		return showCaption;
	}

	public void setShowCaption(boolean showCaption) {
		this.showCaption = showCaption;
	}

	@ViewAttribute(defaultValue = "false")
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Boolean isChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public boolean isTriState() {
		return triState;
	}

	public void setTriState(boolean triState) {
		this.triState = triState;
	}
}
