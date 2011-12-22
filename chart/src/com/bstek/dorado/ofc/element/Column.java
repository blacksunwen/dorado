package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.binding.ColumnBindingConfig;

@ClientObject(prototype = "dorado.widget.ofc.Column",
		shortTypeName = "ofc.Column")
public class Column extends Element {
	private Double barWidth;
	private String color;
	private ColumnChartType type;
	private ShowAnimation showAnimation;
	private ColumnBindingConfig bindingConfig;

	/**
	 * @return the bindingConfig
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public ColumnBindingConfig getBindingConfig() {
		return bindingConfig;
	}

	/**
	 * @param bindingConfig
	 *            the bindingConfig to set
	 */
	public void setBindingConfig(ColumnBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}

	/**
	 * @return the barWidth
	 */
	public Double getBarWidth() {
		return barWidth;
	}

	/**
	 * @param barWidth
	 *            the barWidth to set
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
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the type
	 */
	public ColumnChartType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ColumnChartType type) {
		this.type = type;
	}

	/**
	 * @return the showAnimation
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public ShowAnimation getShowAnimation() {
		return showAnimation;
	}

	/**
	 * @param showAnimation
	 *            the showAnimation to set
	 */
	public void setShowAnimation(ShowAnimation showAnimation) {
		this.showAnimation = showAnimation;
	}

	public enum ColumnChartType {
		Column, Glass, Cylinder, CylinderOutline, Dome, Round, RoundGlass, Round3D, Column3D, Plastic, PlasticFlat
	}
}
