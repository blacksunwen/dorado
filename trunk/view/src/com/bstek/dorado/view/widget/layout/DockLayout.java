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
package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ClientObject;

/**
 * Border型布局管理器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 5, 2008
 */
@ClientObject(shortTypeName = "Dock")
public class DockLayout extends Layout {
	private int regionPadding;

	public int getRegionPadding() {
		return regionPadding;
	}

	public void setRegionPadding(int regionPadding) {
		this.regionPadding = regionPadding;
	}
}
