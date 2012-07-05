package com.bstek.dorado.view.widget.base.tab;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
@Widget(name = "TabBar", category = "General", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.TabBar", shortTypeName = "TabBar")
public class TabBar extends AbstractTabControl {
	private TabPlacement tabPlacement = TabPlacement.top;
	private boolean showMenuButton;
	private int tabMinWidth;
	
	@ClientProperty(escapeValue = "top")
	public TabPlacement getTabPlacement() {
		return tabPlacement;
	}

	public void setTabPlacement(TabPlacement tabPlacement) {
		this.tabPlacement = tabPlacement;
	}
	
	public boolean isShowMenuButton() {
		return showMenuButton;
	}

	public void setShowMenuButton(boolean showMenuButton) {
		this.showMenuButton = showMenuButton;
	}

	public int getTabMinWidth() {
		return tabMinWidth;
	}

	public void setTabMinWidth(int tabMinWidth) {
		this.tabMinWidth = tabMinWidth;
	}

}
