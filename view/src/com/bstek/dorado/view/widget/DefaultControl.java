package com.bstek.dorado.view.widget;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-12-27
 */
@Widget(name = "Control", category = "General", dependsPackage = "widget")
@XmlNode(nodeName = "Control")
@ClientObject(prototype = "dorado.widget.Control", shortTypeName = "Control",
		outputter = "spring:dorado.controlOutputter")
public class DefaultControl extends Control {
}
