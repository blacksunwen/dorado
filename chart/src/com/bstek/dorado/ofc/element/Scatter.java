package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.binding.LineDotBindingConfig;

@ClientObject(prototype = "dorado.widget.ofc.Scatter",
		shortTypeName = "ofc.Scatter")
public class Scatter extends Element {
	public enum ScatterType {
		line, point
	}

	private String color;
	private LineDotStyle dotStyle;
	private Integer width;
	private ScatterType type;
	private AxisPosition axis;

	/**
	 * @return the dotStyle
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public LineDotStyle getDotStyle() {
		return dotStyle;
	}

	/**
	 * @param dotStyle
	 *            the dotStyle to set
	 */
	public void setDotStyle(LineDotStyle dotStyle) {
		this.dotStyle = dotStyle;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * @return the axis
	 */
	public AxisPosition getAxis() {
		return axis;
	}

	/**
	 * @param axis
	 *            the axis to set
	 */
	public void setAxis(AxisPosition axis) {
		this.axis = axis;
	}

	private LineDotBindingConfig bindingConfig;

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the type
	 */
	public ScatterType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ScatterType type) {
		this.type = type;
	}

	/**
	 * @return the bindingConfig
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public LineDotBindingConfig getBindingConfig() {
		return bindingConfig;
	}

	/**
	 * @param bindingConfig
	 *            the bindingConfig to set
	 */
	public void setBindingConfig(LineDotBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}

}
