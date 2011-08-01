package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-6
 */
@Widget(name = "GroupBox", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.GroupBox", shortTypeName = "GroupBox")
@XmlNode(nodeName = "GroupBox")
public class GroupBox extends AbstractPanel {}
