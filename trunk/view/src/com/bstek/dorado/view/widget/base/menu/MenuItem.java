package com.bstek.dorado.view.widget.base.menu;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-6
 */
@ViewObject(prototype = "dorado.widget.menu.MenuItem")
@XmlNode(nodeName = "MenuItem")
public class MenuItem extends TextMenuItem implements MenuItemGroup {
}
