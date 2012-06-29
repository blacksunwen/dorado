package com.bstek.dorado.view.widget.datacontrol;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-24
 */
@Widget(name = "DataPilot", category = "General",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.DataPilot",
		shortTypeName = "DataPilot")
@ClientEvents({ @ClientEvent(name = "onSubControlRefresh"),
		@ClientEvent(name = "onSubControlAction") })
public class DataPilot extends AbstractDataControl {
	private String itemCodes;

	@IdeProperty(enumValues = "pages,|<,<,>,>|,goto,pageSize,info,+,-,x,|", highlight = 1)
	public String getItemCodes() {
		return itemCodes;
	}

	public void setItemCodes(String itemCodes) {
		this.itemCodes = itemCodes;
	}
}
