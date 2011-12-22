package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
@Widget(name = "IFrame", category = "General", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.IFrame", shortTypeName = "IFrame")
@ClientEvents({ @ClientEvent(name = "onLoad") })
public class IFrame extends Control {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
