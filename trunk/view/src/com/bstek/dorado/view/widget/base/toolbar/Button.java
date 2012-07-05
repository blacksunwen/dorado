package com.bstek.dorado.view.widget.base.toolbar;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "ToolBarButton", category = "ToolBar",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.toolbar.ToolBarButton",
		shortTypeName = "ToolBarButton")
@XmlNode(nodeName = "ToolBarButton")
public class Button extends com.bstek.dorado.view.widget.base.Button {
	private boolean showMenuOnHover;
		
	@ClientProperty(escapeValue = "false")
	public boolean isShowMenuOnHover() {
		return showMenuOnHover;
	}

	public void setShowMenuOnHover(boolean showMenuOnHover) {
		this.showMenuOnHover = showMenuOnHover;
	}
	
}
