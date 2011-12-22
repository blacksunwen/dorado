package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.XmlNode;

@XmlNode
public class Shortcut {
	private String icon;
	private String iconClass;
	private ShortcutIconSize iconSize;
	private String caption;
	private String appId;

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
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
	 * @param iconClass
	 *            the iconClass to set
	 */
	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	/**
	 * @return the iconSize
	 */
	public ShortcutIconSize getIconSize() {
		return iconSize;
	}

	/**
	 * @param iconSize
	 *            the iconSize to set
	 */
	public void setIconSize(ShortcutIconSize iconSize) {
		this.iconSize = iconSize;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
