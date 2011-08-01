package com.bstek.dorado.view.widget.base.toolbar;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "ToolBar.ToolBarButton", category = "ToolBar", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.toolbar.ToolBarButton", shortTypeName = "ToolBarButton")
@XmlNode(nodeName = "ToolBarButton")
public class Button extends com.bstek.dorado.view.widget.base.Button {
}
