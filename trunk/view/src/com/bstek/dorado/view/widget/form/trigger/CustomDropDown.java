package com.bstek.dorado.view.widget.form.trigger;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementReference;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "CustomDropDown", category = "Trigger", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.CustomDropDown", shortTypeName = "CustomDropDown")
@XmlNode(nodeName = "CustomDropDown")
public class CustomDropDown extends DropDown {
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
