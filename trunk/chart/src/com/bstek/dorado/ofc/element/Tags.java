package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.binding.TagBindingConfig;

@ClientObject(prototype = "dorado.widget.ofc.Tags", shortTypeName = "ofc.Tags")
public class Tags extends Element {
	public enum TagsAlignX {
		center, left, right
	}

	public enum TagsAlignY {
		above, below, center
	}

	private String color;
	private TagsAlignX alignX;
	private TagsAlignY alignY;
	private Double padX;
	private Double padY;
	private String font;
	private boolean bold;
	private Double rotate;
	private String text;
	private boolean border;
	private boolean underline;
	private Double alpha;
	private AxisPosition axis;
	private TagBindingConfig bindingConfig;

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
	 * @return the alignX
	 */
	public TagsAlignX getAlignX() {
		return alignX;
	}

	/**
	 * @param alignX
	 *            the alignX to set
	 */
	public void setAlignX(TagsAlignX alignX) {
		this.alignX = alignX;
	}

	/**
	 * @return the alignY
	 */
	public TagsAlignY getAlignY() {
		return alignY;
	}

	/**
	 * @param alignY
	 *            the alignY to set
	 */
	public void setAlignY(TagsAlignY alignY) {
		this.alignY = alignY;
	}

	/**
	 * @return the padX
	 */
	public Double getPadX() {
		return padX;
	}

	/**
	 * @param padX
	 *            the padX to set
	 */
	public void setPadX(Double padX) {
		this.padX = padX;
	}

	/**
	 * @return the padY
	 */
	public Double getPadY() {
		return padY;
	}

	/**
	 * @param padY
	 *            the padY to set
	 */
	public void setPadY(Double padY) {
		this.padY = padY;
	}

	/**
	 * @return the font
	 */
	public String getFont() {
		return font;
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(String font) {
		this.font = font;
	}

	/**
	 * @return the bold
	 */
	public boolean isBold() {
		return bold;
	}

	/**
	 * @param bold
	 *            the bold to set
	 */
	public void setBold(boolean bold) {
		this.bold = bold;
	}

	/**
	 * @return the rotate
	 */
	public Double getRotate() {
		return rotate;
	}

	/**
	 * @param rotate
	 *            the rotate to set
	 */
	public void setRotate(Double rotate) {
		this.rotate = rotate;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the border
	 */
	public boolean isBorder() {
		return border;
	}

	/**
	 * @param border
	 *            the border to set
	 */
	public void setBorder(boolean border) {
		this.border = border;
	}

	/**
	 * @return the underline
	 */
	public boolean isUnderline() {
		return underline;
	}

	/**
	 * @param underline
	 *            the underline to set
	 */
	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha
	 *            the alpha to set
	 */
	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return the bindingConfig
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public TagBindingConfig getBindingConfig() {
		return bindingConfig;
	}

	/**
	 * @param bindingConfig
	 *            the bindingConfig to set
	 */
	public void setBindingConfig(TagBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
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
}
