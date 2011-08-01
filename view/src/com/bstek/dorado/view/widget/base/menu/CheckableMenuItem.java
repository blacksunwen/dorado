package com.bstek.dorado.view.widget.base.menu;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-6
 */
@ViewObject(prototype = "dorado.widget.menu.CheckableMenuItem", shortTypeName = "Checkable")
@XmlNode(nodeName = "CheckableMenuItem")
@ClientEvents( { @ClientEvent(name = "onCheckedChange") })
public class CheckableMenuItem extends MenuItem {
	private boolean checked;
	private String group;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
