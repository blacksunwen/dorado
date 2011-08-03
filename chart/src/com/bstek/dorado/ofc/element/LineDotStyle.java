package com.bstek.dorado.ofc.element;

public class LineDotStyle {
	public enum LineDotType {
		Anchor, Bow, Dot, StarDot, HollowDot, SolidDot
	}
	
	private LineDotType type;
	private Integer dotSize;
	private Integer haloSize;
	private Double width;
	private Double alpha;
	private String backgroundColor;
	private Double backgroundAlpha;
	private String color;
	private Integer sides;
	private boolean hollow;
	private Integer rotation;
	private String toolTip;
	private AxisPosition axis;
	
	/**
	 * @return the type
	 */
	public LineDotType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(LineDotType type) {
		this.type = type;
	}
	/**
	 * @return the dotSize
	 */
	public Integer getDotSize() {
		return dotSize;
	}
	/**
	 * @param dotSize the dotSize to set
	 */
	public void setDotSize(Integer dotSize) {
		this.dotSize = dotSize;
	}
	/**
	 * @return the haloSize
	 */
	public Integer getHaloSize() {
		return haloSize;
	}
	/**
	 * @param haloSize the haloSize to set
	 */
	public void setHaloSize(Integer haloSize) {
		this.haloSize = haloSize;
	}
	/**
	 * @return the width
	 */
	public Double getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(Double width) {
		this.width = width;
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
	 * @return the backgroudColor
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}
	/**
	 * @param backgroudColor the backgroudColor to set
	 */
	public void setBackgroundColor(String backgroudColor) {
		this.backgroundColor = backgroudColor;
	}
	
	/**
	 * @return the backgroundAlpha
	 */
	public Double getBackgroundAlpha() {
		return backgroundAlpha;
	}
	/**
	 * @param backgroundAlpha the backgroundAlpha to set
	 */
	public void setBackgroundAlpha(Double backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
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
	/**
	 * @return the sides
	 */
	public Integer getSides() {
		return sides;
	}
	/**
	 * @param sides the sides to set
	 */
	public void setSides(Integer sides) {
		this.sides = sides;
	}
	/**
	 * @return the hollow
	 */
	public boolean isHollow() {
		return hollow;
	}
	/**
	 * @param hollow the hollow to set
	 */
	public void setHollow(boolean hollow) {
		this.hollow = hollow;
	}
	/**
	 * @return the rotation
	 */
	public Integer getRotation() {
		return rotation;
	}
	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}
	/**
	 * @return the toolTip
	 */
	public String getToolTip() {
		return toolTip;
	}
	/**
	 * @param toolTip the toolTip to set
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	/**
	 * @return the axis
	 */
	public AxisPosition getAxis() {
		return axis;
	}
	/**
	 * @param axis the axis to set
	 */
	public void setAxis(AxisPosition axis) {
		this.axis = axis;
	}
	
	
}