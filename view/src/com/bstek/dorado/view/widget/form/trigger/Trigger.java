package com.bstek.dorado.view.widget.form.trigger;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.Component;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-20
 */
@Widget(name = "Trigger", category = "Trigger", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.Trigger", shortTypeName = "Trigger")
@XmlNode(nodeName = "Trigger")
@ClientEvents( { @ClientEvent(name = "onExecute") })
public class Trigger extends Component {
	private String icon;
	private String iconClass;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@ViewAttribute(enumValues = "d-trigger-icon-drop,d-trigger-icon-search,d-trigger-icon-date,d-trigger-icon-custom")
	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}
}
