package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 16, 2009
 */
@Widget(name = "Button", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.Button", shortTypeName = "Button")
@XmlNode(nodeName = "Button")
@ClientEvents({ @ClientEvent(name = "onTriggerClick") })
public class Button extends AbstractButton {
	private String caption;
	private String icon;
	private String iconClass;
	private boolean triggerToggled;
	private double num;
	private Boolean showTrigger;
	private Boolean splitButton;

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

	public Boolean getShowTrigger() {
		return showTrigger;
	}

	public void setShowTrigger(Boolean showTrigger) {
		this.showTrigger = showTrigger;
	}

	public Boolean getSplitButton() {
		return splitButton;
	}

	public void setSplitButton(Boolean splitButton) {
		this.splitButton = splitButton;
	}
	
	

}
