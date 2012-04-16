package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 16, 2009
 */
@Widget(name = "Button", category = "General", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.Button", shortTypeName = "Button")
@ClientEvents({ @ClientEvent(name = "onTriggerClick") })
public class Button extends AbstractButton {
	private String caption;
	private String icon;
	private String iconClass;
	private boolean triggerToggled;
	private double num;
	private boolean showTrigger;
	private boolean splitButton;

	@IdeProperty(highlight = 1)
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

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

	public boolean isTriggerToggled() {
		return triggerToggled;
	}

	public void setTriggerToggled(boolean triggerToggled) {
		this.triggerToggled = triggerToggled;
	}

	public double getNum() {
		return num;
	}

	public void setNum(double num) {
		this.num = num;
	}

	public boolean getShowTrigger() {
		return showTrigger;
	}

	public void setShowTrigger(boolean showTrigger) {
		this.showTrigger = showTrigger;
	}

	public boolean getSplitButton() {
		return splitButton;
	}

	public void setSplitButton(boolean splitButton) {
		this.splitButton = splitButton;
	}

}
