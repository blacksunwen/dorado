package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.Control;

@Widget(name = "Flash", category = "Desktop", dependsPackage = "desktop")
@ViewObject(prototype = "dorado.widget.Flash", shortTypeName = "Flash")
@XmlNode(nodeName = "Flash")
public class Flash extends Control {
	private String path;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
}
