package com.bstek.dorado.view.widget.base.toolbar;


import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "ToolBar.Separator", category = "ToolBar", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.toolbar.Separator", shortTypeName = "Separator")
@XmlNode(nodeName = "Separator")
public class Separator extends Control {

}
