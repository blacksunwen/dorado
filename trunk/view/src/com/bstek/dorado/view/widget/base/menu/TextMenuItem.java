package com.bstek.dorado.view.widget.base.menu;

import java.util.List;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.ResourceInjection;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.common.event.ClientEventSupported;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.widget.InnerElementList;
import com.bstek.dorado.view.widget.action.ActionSupport;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
@ClientEvents({ @ClientEvent(name = "onClick") })
@ResourceInjection(subObjectMethod = "getItem")
public abstract class TextMenuItem extends BaseMenuItem implements
		MenuItemGroup, ActionSupport, ClientEventSupported {
	private String caption;
	private String icon;
	private String iconClass;
	private String action;
	private boolean disabled;
	private boolean hideOnClick = true;

	private List<BaseMenuItem> menuItems = new InnerElementList<BaseMenuItem>(
			this);

	public void addItem(BaseMenuItem menuItem) {
		menuItems.add(menuItem);
	}

	public BaseMenuItem getItem(String name) {
		for (BaseMenuItem item : menuItems) {
			if (name.equals(item.getName())) {
				return item;
			}
		}
		return null;
	}

	@XmlSubNode
	@ClientProperty
	public List<BaseMenuItem> getItems() {
		return menuItems;
	}

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

	@ComponentReference("Action")
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

	@ClientProperty(escapeValue = "true")
	public boolean isHideOnClick() {
		return hideOnClick;
	}

	public void setHideOnClick(boolean hideOnClick) {
		this.hideOnClick = hideOnClick;
	}

}
