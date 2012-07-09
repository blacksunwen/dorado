package com.bstek.dorado.view.widget.base.tab;

import com.bstek.dorado.annotation.ClientProperty;

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
