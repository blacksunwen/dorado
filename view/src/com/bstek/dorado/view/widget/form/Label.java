package com.bstek.dorado.view.widget.form;


import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "Label", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.Label", shortTypeName = "Label")
@XmlNode(nodeName = "Label")
public class Label extends Control {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
