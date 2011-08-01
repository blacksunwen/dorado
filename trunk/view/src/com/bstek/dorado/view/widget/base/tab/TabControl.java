package com.bstek.dorado.view.widget.base.tab;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */

@Widget(name = "TabControl", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.TabControl", shortTypeName = "TabControl")
@XmlNode(nodeName = "TabControl")
public class TabControl extends AbstractTabControl {
	@Override
	@ViewAttribute
	@XmlSubNode(parser = "dorado.TabControl.TabsParser", path = "#self")
	public List<Tab> getTabs() {
		return super.getTabs();
	}
}
