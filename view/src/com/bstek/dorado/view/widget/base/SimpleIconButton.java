package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-1
 */
@Widget(name = "SimpleIconButton", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.SimpleIconButton", shortTypeName = "SimpleIconButton")
@XmlNode(nodeName = "SimpleIconButton")
public class SimpleIconButton extends SimpleButton {
	private String icon;
	private String iconClass;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}
}
