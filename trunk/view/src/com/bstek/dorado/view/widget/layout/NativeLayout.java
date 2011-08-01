package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ViewObject;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-20
 */
@ViewObject(shortTypeName = "Native")
public class NativeLayout extends Layout {
	private String style;

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
