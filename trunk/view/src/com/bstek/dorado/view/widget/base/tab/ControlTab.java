package com.bstek.dorado.view.widget.base.tab;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementReference;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-4
 */
@XmlNode(nodeName = "ControlTab")
@ViewObject(prototype = "dorado.widget.tab.ControlTab", shortTypeName = "Control")
public class ControlTab extends Tab {
	private InnerElementReference<Control> controlRef = new InnerElementReference<Control>(
			this);

	@ViewAttribute
	@XmlSubNode(path = "*")
	public Control getControl() {
		return controlRef.get();
	}

	public void setControl(Control control) {
		controlRef.set(control);
	}
}
