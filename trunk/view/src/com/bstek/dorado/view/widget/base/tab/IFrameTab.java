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
import com.bstek.dorado.annotation.IdeProperty;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
@ClientObject(prototype = "dorado.widget.tab.IFrameTab",
		shortTypeName = "IFrame")
public class IFrameTab extends Tab {
	private String path;

	@IdeProperty(highlight = 1)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
