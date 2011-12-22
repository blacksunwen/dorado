package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.action.ActionSupport;

public abstract class AbstractButton extends Control implements ActionSupport {
	private String action;
	private boolean disabled;
	private boolean toggleable;
	private boolean toggled;
	private String menu;
	private boolean toggleOnShowMenu = true;

	@ComponentReference("Action")
	@IdeProperty(highlight = 1)
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

	@ComponentReference("Menu")
	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isToggleOnShowMenu() {
		return toggleOnShowMenu;
	}

	public void setToggleOnShowMenu(boolean toggleOnShowMenu) {
		this.toggleOnShowMenu = toggleOnShowMenu;
	}

}