package com.bstek.dorado.view.widget.base.menu;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-6
 */
@ClientObject(prototype = "dorado.widget.menu.MenuItem",
		shortTypeName = "Default")
public class MenuItem extends TextMenuItem implements MenuItemGroup {

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

}
