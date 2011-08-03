package com.bstek.dorado.desktop;

import com.bstek.dorado.view.widget.Control;

public class TaskButton extends Control {
	private String caption;
	private String icon;
	private String iconClass;
	
	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the iconClass
	 */
	public String getIconClass() {
		return iconClass;
	}
	/**
	 * @param iconClass the iconClass to set
	 */
	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}
	
}
