package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.widget.Align;

public class VBoxLayoutConstraintSupport extends LayoutConstraintSupport {
	private Align align = Align.left;

	@ClientProperty(escapeValue = "left")
	public Align getAlign() {
		return align;
	}

	public void setAlign(Align align) {
		this.align = align;
	}
}
