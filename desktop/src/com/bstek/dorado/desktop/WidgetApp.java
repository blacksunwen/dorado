package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementReference;

@ClientObject(prototype = "dorado.widget.desktop.WidgetApp",
		shortTypeName = "Widget")
public class WidgetApp extends AbstractControlApp {
	private InnerElementReference<Control> controlRef = new InnerElementReference<Control>(
			this);

	/**
	 * @return
	 */
	@XmlSubNode(fixed = true)
	@ClientProperty
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
