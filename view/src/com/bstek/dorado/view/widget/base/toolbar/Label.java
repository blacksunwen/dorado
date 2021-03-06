/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.widget.base.toolbar;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(category = "ToolBar")
@XmlNode(nodeName = "ToolBarLabel")
@ClientObject(prototype = "dorado.widget.toolbar.ToolBarLabel", shortTypeName = "Label")
public class Label extends com.bstek.dorado.view.widget.Control {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
