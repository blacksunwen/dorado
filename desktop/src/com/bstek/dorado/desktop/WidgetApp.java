package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementReference;

@XmlNode(nodeName = "WidgetApp")
@ViewObject(prototype = "dorado.widget.desktop.WidgetApp", shortTypeName = "Widget")
public class WidgetApp extends AbstractControlApp {
	private InnerElementReference<Control> controlRef = new InnerElementReference<Control>(this);

	/**
	 * @return
	 */
	@ViewAttribute
	@XmlSubNode(path = "*", fixed = true)
	public Control getControl() {
		return controlRef.get();
	}

	/**
	 * @param control
	 */
	public void setControl(Control control) {
		controlRef.set(control);
	}
}
