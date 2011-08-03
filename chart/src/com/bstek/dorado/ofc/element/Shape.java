package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.binding.PointBindingConfig;

@ViewObject(prototype = "dorado.widget.ofc.Shape", shortTypeName = "ofc.Shape")
@XmlNode(nodeName = "Shape")
public class Shape extends Element{
	private Double alpha;
	private String text;
	private PointBindingConfig bindingConfig;
	/**
	 * @return the bindingConfig
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public PointBindingConfig getBindingConfig() {
		return bindingConfig;
	}
	/**
	 * @param bindingConfig the bindingConfig to set
	 */
	public void setBindingConfig(PointBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}
	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		return alpha;
	}
	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	
}
