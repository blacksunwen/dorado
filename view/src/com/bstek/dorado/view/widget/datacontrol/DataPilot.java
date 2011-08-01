package com.bstek.dorado.view.widget.datacontrol;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-24
 */
@Widget(name = "DataPilot", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.DataPilot", shortTypeName = "DataPilot")
@XmlNode(nodeName = "DataPilot")
@ClientEvents( { @ClientEvent(name = "onSubControlRefresh"),
		@ClientEvent(name = "onSubControlAction") })
public class DataPilot extends AbstractDataControl {
	private String itemCodes;

	@ViewAttribute(enumValues = "pages,|<,<,>,>|,goto,+,-,x,|")
	public String getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(String itemCodes) {
		this.itemCodes = itemCodes;
	}
}
