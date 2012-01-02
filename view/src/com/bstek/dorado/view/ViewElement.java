package com.bstek.dorado.view;

import java.util.Collection;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-25
 */
public interface ViewElement {

	void setParent(ViewElement parent);

	ViewElement getParent();

	void registerInnerElement(ViewElement element);

	void unregisterInnerElement(ViewElement element);

	Collection<ViewElement> getInnerElements();
}
