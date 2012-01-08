package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-1
 */
@Widget(name = "SimpleIconButton", category = "General",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.SimpleIconButton",
		shortTypeName = "SimpleIconButton")
public class SimpleIconButton extends SimpleButton {
	private String icon;
	private String iconClass;

	@IdeProperty(highlight = 1)
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
