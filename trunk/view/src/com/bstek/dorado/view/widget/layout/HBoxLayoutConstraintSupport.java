package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.widget.VerticalAlign;

public class HBoxLayoutConstraintSupport extends LayoutConstraintSupport {
	private VerticalAlign align = VerticalAlign.center;

	@ClientProperty(escapeValue = "center")
	public VerticalAlign getAlign() {
		return align;
	}

	public void setAlign(VerticalAlign align) {
		this.align = align;
	}
}
