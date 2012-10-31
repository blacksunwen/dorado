/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
