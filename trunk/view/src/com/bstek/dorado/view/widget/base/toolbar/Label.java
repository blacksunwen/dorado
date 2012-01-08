package com.bstek.dorado.view.widget.base.toolbar;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "ToolBarLabel", category = "ToolBar",
		dependsPackage = "base-widget")
@XmlNode(nodeName = "ToolBarLabel")
@ClientObject(prototype = "dorado.widget.toolbar.ToolBarLabel",
		shortTypeName = "ToolBarLabel")
public class Label extends com.bstek.dorado.view.widget.form.Label {

}
