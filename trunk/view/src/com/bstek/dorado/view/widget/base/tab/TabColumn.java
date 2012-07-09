package com.bstek.dorado.view.widget.base.tab;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;

@Widget(name = "TabColumn", category = "General", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.TabColumn", shortTypeName = "TabColumn")
public class TabColumn extends AbstractTabControl{
	private VerticalTabPlacement tabPlacement = VerticalTabPlacement.left;
	
	@ClientProperty(escapeValue = "left")
	public VerticalTabPlacement getTabPlacement() {
		return tabPlacement;
	}
	
	public void setTabPlacement(VerticalTabPlacement tabPlacement) {
		this.tabPlacement = tabPlacement;
	}
}
