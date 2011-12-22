package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "ProgressBar", category = "General",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.ProgressBar",
		shortTypeName = "ProgressBar")
public class ProgressBar extends Control {
	private float minValue;
	private float maxValue = 100;
	private String value;
	private String textPattern = "${percent}%";
	private boolean showText = true;
	private boolean effectEnable = false;

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	@ClientProperty(escapeValue = "100")
	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ClientProperty(escapeValue = "{percent}%")
	public String getTextPattern() {
		return textPattern;
	}

	public void setTextPattern(String textPattern) {
		this.textPattern = textPattern;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isShowText() {
		return showText;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public boolean isEffectEnable() {
		return effectEnable;
	}

	public void setEffectEnable(boolean effectEnable) {
		this.effectEnable = effectEnable;
	}

}
