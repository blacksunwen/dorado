package com.bstek.dorado.ofc;


public class ToolTip {
	private String backgroundColor;
	private String bodyStyle;
	private String color;
	private boolean shadow;
	private int stroke;
	private String titleStyle;
	private MouseStyle mouse;
	
	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBodyStyle() {
		return bodyStyle;
	}

	public void setBodyStyle(String bodyStyle) {
		this.bodyStyle = bodyStyle;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isShadow() {
		return shadow;
	}

	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}

	public int getStroke() {
		return stroke;
	}

	public void setStroke(int stroke) {
		this.stroke = stroke;
	}

	public String getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(String titleStyle) {
		this.titleStyle = titleStyle;
	}

	public MouseStyle getMouse() {
		return mouse;
	}

	public void setMouse(MouseStyle mouse) {
		this.mouse = mouse;
	}

	public static enum MouseStyle {
		Closest,Proximity,Normal
	}
	
}

