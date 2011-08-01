/**
 * 
 */
package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.view.widget.Align;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-20
 */
@ViewObject(shortTypeName = "VBox")
public class VBoxLayout extends AbstractBoxLayout {
	private Align align = Align.left;

	@ViewAttribute(defaultValue = "left")
	public Align getAlign() {
		return align;
	}

	public void setAlign(Align align) {
		this.align = align;
	}
}
