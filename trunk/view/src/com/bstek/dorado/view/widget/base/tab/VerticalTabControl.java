/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
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
