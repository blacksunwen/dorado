package com.bstek.dorado.view.widget.base.tab;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */

@Widget(name = "VerticalTabControl", category = "General",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.VerticalTabControl",
		shortTypeName = "VerticalTabControl")
public class VerticalTabControl extends TabColumn {
	private int tabColumnWidth = 200;
	
	@ClientProperty(escapeValue = "200")
	public int getTabColumnWidth() {
		return tabColumnWidth;
	}

	public void setTabColumnWidth(int tabColumnWidth) {
		this.tabColumnWidth = tabColumnWidth;
	}
	
}
