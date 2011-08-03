package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.binding.BarBindingConfig;

@ViewObject(prototype = "dorado.widget.ofc.Bar", shortTypeName = "ofc.Bar")
@XmlNode(nodeName = "Bar")
public class Bar extends Element{
	private Double barWidth;
	private String color;
	private BarBindingConfig bindingConfig;
	/**
	 * @return the bindingConfig
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public BarBindingConfig getBindingConfig() {
		return bindingConfig;
	}
	/**
	 * @param bindingConfig the bindingConfig to set
	 */
	public void setBindingConfig(BarBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}
	/**
	 * @return the barWidth
	 */
	public Double getBarWidth() {
		return barWidth;
	}
	/**
	 * @param barWidth the barWidth to set
	 */
	public void setBarWidth(Double barWidth) {
		this.barWidth = barWidth;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
}
