package com.bstek.dorado.desktop;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.Widget;

@Widget(name = "Desktop")
@ClientObject(prototype = "dorado.widget.desktop.Desktop",
		shortTypeName = "desktop.Desktop")
public class Desktop extends AbstractDesktop {
	private ShortcutIconSize iconSize;
	private List<Shortcut> items;
	private String navtip;
	
	public String getNavtip() {
		return navtip;
	}

	public void setNavtip(String navtip) {
		this.navtip = navtip;
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
	 * @return the items
	 */
	@XmlSubNode
	@ClientProperty
	public List<Shortcut> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<Shortcut> items) {
		this.items = items;
	}

}
