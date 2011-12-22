package com.bstek.dorado.ofc;

import com.bstek.dorado.annotation.XmlNode;

@XmlNode
public class Key {
	private String color;
	private int fontSize;
	private String text;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
