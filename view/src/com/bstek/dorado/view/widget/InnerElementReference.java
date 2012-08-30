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
