package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ViewObject;

/**
 * Border型布局管理器。
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 5, 2008
 */
@ViewObject(shortTypeName = "Dock")
public class DockLayout extends Layout {
	private int regionPadding;

	public int getRegionPadding() {
		return regionPadding;
	}

	public void setRegionPadding(int regionPadding) {
		this.regionPadding = regionPadding;
	}
}
