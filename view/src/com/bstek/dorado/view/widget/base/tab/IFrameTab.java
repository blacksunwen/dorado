package com.bstek.dorado.view.widget.base.tab;

import com.bstek.dorado.annotation.ClientObject;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
@ClientObject(prototype = "dorado.widget.tab.IFrameTab",
		shortTypeName = "IFrame")
public class IFrameTab extends Tab {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
