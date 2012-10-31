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
