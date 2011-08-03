package com.bstek.dorado.ofc;

import com.bstek.dorado.annotation.XmlNode;

@XmlNode(nodeName = "Text")
public class Text {
	private String style;
	private String text;
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
