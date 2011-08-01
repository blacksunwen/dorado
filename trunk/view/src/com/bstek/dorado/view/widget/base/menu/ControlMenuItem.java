package com.bstek.dorado.view.widget.base.menu;

import java.util.Collection;
import java.util.HashSet;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementReference;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-6
 */
@ViewObject(prototype = "dorado.widget.menu.ControlMenuItem", shortTypeName = "Control")
@XmlNode(nodeName = "ControlMenuItem")
public class ControlMenuItem extends BaseMenuItem implements ViewElement {
	private InnerElementReference<Control> controlRef = new InnerElementReference<Control>(
			this);
	private Collection<ViewElement> innerElements = new HashSet<ViewElement>();

	@ViewAttribute
	@XmlSubNode(path = "Control/*", fixed = true)
	public Control getControl() {
		return controlRef.get();
	}

	public void setControl(Control control) {
		controlRef.set(control);
	}

	public void registerInnerElement(ViewElement element) {
		innerElements.add(element);
	}

	public void unregisterInnerElement(ViewElement element) {
		innerElements.remove(element);
	}

	public Collection<ViewElement> getInnerElements() {
		return innerElements;
	}
}
