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
package com.bstek.dorado.view.widget;

import com.bstek.dorado.view.ViewElement;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-27
 */
public class InnerElementReference<E extends ViewElement> {
	private ViewElement parent;
	private E element;

	public InnerElementReference(ViewElement parent) {
		this.parent = parent;
	}

	public void set(E element) {
		if (this.element != null) {
			this.element.setParent(null);
			parent.unregisterInnerElement(this.element);
		}
		this.element = element;
		if (element != null) {
			parent.registerInnerElement(element);
			element.setParent(parent);
		}
	}

	public E get() {
		return element;
	}
}
