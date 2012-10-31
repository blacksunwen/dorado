/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
package com.bstek.dorado.view.widget.base.menu;

import java.util.Collection;
import java.util.HashSet;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.FloatControl;
import com.bstek.dorado.view.widget.InnerElementReference;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-6
 */
@ClientObject(prototype = "dorado.widget.menu.ControlMenuItem",
		shortTypeName = "Control")
public class ControlMenuItem extends TextMenuItem implements ViewElement {
	private InnerElementReference<Control> controlRef = new InnerElementReference<Control>(
			this);
	private Collection<ViewElement> innerElements = new HashSet<ViewElement>();

	@XmlSubNode
	@ClientProperty
	public FloatControl getControl() {
		return (FloatControl) controlRef.get();
	}

	public void setControl(FloatControl control) {
		controlRef.set((Control) control);
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
