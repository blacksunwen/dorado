package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.binding.LineDotBindingConfig;

@ViewObject(prototype = "dorado.widget.ofc.Line", shortTypeName = "ofc.Line")
@XmlNode(nodeName = "Line")
public class Line extends Element{
	public class LineStyle {
		private LineStyleStyle style;
		private Double on;
		private Double off;
		/**
		 * @return the style
		 */
		public LineStyleStyle getStyle() {
			return style;
		}
		/**
		 * @param style the style to set
		 */
		public void setStyle(LineStyleStyle style) {
			this.style = style;
		}
		/**
		 * @return the on
		 */
		public Double getOn() {
			return on;
		}
		/**
		 * @param on the on to set
		 */
		public void setOn(Double on) {
			this.on = on;
		}
		/**
		 * @return the off
		 */
		public Double getOff() {
			return off;
		}
		/**
		 * @param off the off to set
		 */
		public void setOff(Double off) {
			this.off = off;
		}
	}
	
	public enum LineStyleStyle {
		solid, dash
	}
	
	private String color;
	private String text;
	private int width;
	
	private AxisPosition axis;
	private LineStyle lineStyle;
	private LineDotStyle dotStyle;
	private ShowAnimation showAnimation;
	private LineDotBindingConfig bindingConfig;
	
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
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * @return the rightAxis
	 */
	public AxisPosition getAxis() {
		return axis;
	}
	
	/**
	 * @param rightAxis the rightAxis to set
	 */
	public void setAxis(AxisPosition axis) {
		this.axis = axis;
	}
	
	/**
	 * @return the showAnimation
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public ShowAnimation getShowAnimation() {
		return showAnimation;
	}

	/**
	 * @param showAnimation the showAnimation to set
	 */
	public void setShowAnimation(ShowAnimation showAnimation) {
		this.showAnimation = showAnimation;
	}
	
	/**
	 * @return the lineStyle
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public LineStyle getLineStyle() {
		return lineStyle;
	}
	
	/**
	 * @param lineStyle the lineStyle to set
	 */
	public void setLineStyle(LineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}
	
	/**
	 * @return the dotStyle
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public LineDotStyle getDotStyle() {
		return dotStyle;
	}
	
	/**
	 * @param dotStyle the dotStyle to set
	 */
	public void setDotStyle(LineDotStyle dotStyle) {
		this.dotStyle = dotStyle;
	}
	
	/**
	 * @return the bindingConfig
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public LineDotBindingConfig getBindingConfig() {
		return bindingConfig;
	}
	
	/**
	 * @param bindingConfig the bindingConfig to set
	 */
	public void setBindingConfig(LineDotBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}
	
}
