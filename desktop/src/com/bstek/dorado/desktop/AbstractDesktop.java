package com.bstek.dorado.desktop;

import java.util.Collection;
import java.util.HashSet;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.common.event.ClientEventSupportedObject;
import com.bstek.dorado.view.View;
import com.bstek.dorado.view.ViewElement;
import com.bstek.dorado.view.ViewElementUtils;

@ClientEvents({ @ClientEvent(name = "onShortcutContextMenu") })
public class AbstractDesktop extends ClientEventSupportedObject implements
		ViewElement {
	private ViewElement parent;
	private Collection<ViewElement> innerElements;

	public ViewElement getParent() {
		return parent;
	}

	public void setParent(ViewElement parent) {
		ViewElementUtils.clearParentViewElement(this, this.parent);

		this.parent = parent;

		View view = ViewElementUtils.getParentView(parent);
		if (view != null) {
			ViewElementUtils.setParentViewElement(this, parent);
		}
	}

	public void registerInnerElement(ViewElement element) {
		if (innerElements == null) {
			innerElements = new HashSet<ViewElement>();
		}
		innerElements.add(element);
	}

	public void unregisterInnerElement(ViewElement element) {
		if (innerElements != null) {
			innerElements.remove(element);
		}
	}

	public Collection<ViewElement> getInnerElements() {
		return innerElements;
	}
}
