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
package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-1
 */
@Widget(name = "SimpleIconButton", category = "General",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.SimpleIconButton",
		shortTypeName = "SimpleIconButton")
public class SimpleIconButton extends SimpleButton {
	private String icon;
	private String iconClass;
	private boolean showTrigger;

	@IdeProperty(highlight = 1)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}
	
	public boolean getShowTrigger() {
		return showTrigger;
	}

	public void setShowTrigger(boolean showTrigger) {
		this.showTrigger = showTrigger;
	}
}
