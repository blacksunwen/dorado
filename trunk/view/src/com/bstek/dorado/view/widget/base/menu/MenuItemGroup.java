package com.bstek.dorado.view.widget.base.menu;

import java.util.List;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
public interface MenuItemGroup {
	public void addItem(BaseMenuItem menuItem);

	public List<BaseMenuItem> getItems();
}
