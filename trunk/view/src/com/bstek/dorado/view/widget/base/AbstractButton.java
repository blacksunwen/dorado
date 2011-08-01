package com.bstek.dorado.view.widget.base;


import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.action.ActionSupport;

public abstract class AbstractButton extends Control implements ActionSupport {
	private String action;
	private boolean disabled;
	private boolean toggleable;
	private boolean toggled;
	private String menu;
	private boolean toggleOnShowMenu = true;

	@ViewAttribute(referenceComponentName = "Action")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isToggleable() {
		return toggleable;
	}

	public void setToggleable(boolean toggleable) {
		this.toggleable = toggleable;
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	@ViewAttribute(referenceComponentName = "Menu")
	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isToggleOnShowMenu() {
		return toggleOnShowMenu;
	}

	public void setToggleOnShowMenu(boolean toggleOnShowMenu) {
		this.toggleOnShowMenu = toggleOnShowMenu;
	}

}