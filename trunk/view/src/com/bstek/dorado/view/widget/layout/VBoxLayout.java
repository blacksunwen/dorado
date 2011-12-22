/**
 * 
 */
package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.widget.Align;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-20
 */
@ClientObject(shortTypeName = "VBox")
public class VBoxLayout extends AbstractBoxLayout {
	private Align align = Align.left;

	@ClientProperty(escapeValue = "left")
	public Align getAlign() {
		return align;
	}

	public void setAlign(Align align) {
		this.align = align;
	}
}
