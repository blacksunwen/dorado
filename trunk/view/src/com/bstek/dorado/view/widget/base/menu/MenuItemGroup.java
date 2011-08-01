package com.bstek.dorado.view.widget.base.menu;

import java.util.List;

import com.bstek.dorado.view.widget.IconPosition;
import com.bstek.dorado.view.widget.IconSize;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
public interface MenuItemGroup {
	public IconPosition getIconPosition();

	public void setIconPosition(IconPosition iconPosition);

	public IconSize getIconSize();

	public void setIconSize(IconSize iconSize);

	public void addItem(BaseMenuItem menuItem);

	public List<BaseMenuItem> getItems();
}
