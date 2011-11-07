package com.bstek.dorado.desktop;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

@ViewObject(prototype = "dorado.widget.desktop.Desktop", shortTypeName = "desktop.Desktop")
@XmlNode(nodeName = "Desktop")
public class Desktop extends AbstractDesktop {
	private ShortcutIconSize iconSize;
	private List<Shortcut> items;

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
	@ViewAttribute
	@XmlSubNode(path = "#self")
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
