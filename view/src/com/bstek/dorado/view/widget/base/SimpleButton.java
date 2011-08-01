package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-1
 */
@Widget(name = "SimpleButton", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.SimpleButton", shortTypeName = "SimpleButton")
@XmlNode(nodeName = "SimpleButton")
public class SimpleButton extends AbstractButton {
	private String mouseDownClassName;
	private String hoverClassName;
	private String toggledClassName;
	private String disabledClassName;

	public String getMouseDownClassName() {
		return mouseDownClassName;
	}

	public void setMouseDownClassName(String mouseDownClassName) {
		this.mouseDownClassName = mouseDownClassName;
	}

	public String getHoverClassName() {
		return hoverClassName;
	}

	public void setHoverClassName(String hoverClassName) {
		this.hoverClassName = hoverClassName;
	}

	public String getToggledClassName() {
		return toggledClassName;
	}

	public void setToggledClassName(String toggledClassName) {
		this.toggledClassName = toggledClassName;
	}

	public String getDisabledClassName() {
		return disabledClassName;
	}

	public void setDisabledClassName(String disabledClassName) {
		this.disabledClassName = disabledClassName;
	}
}
