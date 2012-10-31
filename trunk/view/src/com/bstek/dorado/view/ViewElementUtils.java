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
/**
 * 
 */
package com.bstek.dorado.view;

import java.util.Collection;

import com.bstek.dorado.view.widget.Component;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-25
 */
public abstract class ViewElementUtils {
	private ViewElementUtils() {
	}

	private static Component getParentComponent(ViewElement parent) {
		while (parent != null && !(parent instanceof Component)) {
			parent = parent.getParent();
		}
		return (parent instanceof Component) ? (Component) parent : null;
	}

	public static View getParentView(ViewElement parent) {
		Component parentComponent = getParentComponent(parent);
		if (parentComponent != null) {
			if (parentComponent instanceof View) {
				return (View) parentComponent;
			} else {
				return parentComponent.getView();
			}
		} else {
			return null;
		}
	}

	private static void unregisterFromView(ViewElement element,
			View view) {
		Collection<ViewElement> innerElements = element.getInnerElements();
		if (innerElements != null) {
			for (ViewElement innerElement : innerElements) {
				unregisterFromView(innerElement, view);
			}
		}
		if (element instanceof Component) {
			view.unregisterComponent((Component) element);
		}
	}

	public static void clearParentViewElement(ViewElement element,
			ViewElement oldParent) {
		if (oldParent == null) {
			return;
		}
		View view = getParentView(oldParent);
		if (view != null) {
			unregisterFromView(element, view);
		}
	}

	private static void registerToView(ViewElement element, View view) {
		if (element instanceof Component) {
			view.registerComponent((Component) element);
		}
		Collection<ViewElement> innerElements = element.getInnerElements();
		if (innerElements != null) {
			for (ViewElement innerElement : innerElements) {
				registerToView(innerElement, view);
			}
		}
	}

	public static void setParentViewElement(ViewElement element,
			ViewElement parent) {
		View view = getParentView(parent);
		if (view != null) {
			registerToView(element, view);
		}
	}
}
