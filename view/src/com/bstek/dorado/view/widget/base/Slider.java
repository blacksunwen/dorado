package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.Orientation;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-7
 */
@Widget(name = "Slider", category = "General", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.Slider", shortTypeName = "Slider")
@ClientEvents({ @ClientEvent(name = "beforeValueChange"),
		@ClientEvent(name = "onValueChange") })
public class Slider extends Control {
	private Orientation orientation = Orientation.horizental;
	private float minValue;
	private float maxValue = 100;
	private float value;
	private int precision;
	private float step;

	@ClientProperty(escapeValue = "horizental")
	public Orientation getOrientation() {
		return orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

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

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public float getStep() {
		return step;
	}

	public void setStep(float step) {
		this.step = step;
	}
}
