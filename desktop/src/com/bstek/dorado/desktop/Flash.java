package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;

@Widget(name = "Flash", category = "Advance", dependsPackage = "desktop")
@ClientObject(prototype = "dorado.widget.Flash", shortTypeName = "Flash")
public class Flash extends Control {
	private String path;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
