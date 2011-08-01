package com.bstek.dorado.view.widget.base.toolbar;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementList;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "ToolBar", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.ToolBar", shortTypeName = "ToolBar")
@XmlNode(nodeName = "ToolBar")
public class ToolBar extends Control {
	private List<Control> items = new InnerElementList<Control>(this);
	private boolean fixRight;

	public void addItem(Control item) {
		items.add(item);
	}

	@ViewAttribute
	@XmlSubNode(parser = "dorado.ToolBar.ItemsParser", path = "#self")
	public List<Control> getItems() {
		return items;
	}

	public boolean isFixRight() {
		return fixRight;
	}

	public void setFixRight(boolean fixRight) {
		this.fixRight = fixRight;
	}
}
