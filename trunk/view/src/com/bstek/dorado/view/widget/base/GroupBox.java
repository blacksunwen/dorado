package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-6
 */
@Widget(name = "GroupBox", category = "General", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.GroupBox", shortTypeName = "GroupBox")
public class GroupBox extends AbstractPanel {
	public GroupBox() {
		this.setCollapseable(true);
	}

	@ClientProperty(escapeValue = "true")
	@Override
	public void setCollapseable(boolean collapseable) {
		super.setCollapseable(collapseable);
	}

}
