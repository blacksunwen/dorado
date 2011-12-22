/**
 * 
 */
package com.bstek.dorado.view.widget.layout;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.widget.VerticalAlign;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-7-20
 */
@ClientObject(shortTypeName = "HBox")
public class HBoxLayout extends AbstractBoxLayout {
	private VerticalAlign align = VerticalAlign.center;

	@ClientProperty(escapeValue = "center")
	public VerticalAlign getAlign() {
		return align;
	}

	public void setAlign(VerticalAlign align) {
		this.align = align;
	}
}
