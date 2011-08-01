package com.bstek.dorado.view;

import java.util.Collection;

import com.bstek.dorado.common.ParentAware;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-4-25
 */
public interface ViewElement extends ParentAware<ViewElement> {

	void setParent(ViewElement parent);

	ViewElement getParent();

	void registerInnerElement(ViewElement element);

	void unregisterInnerElement(ViewElement element);

	Collection<ViewElement> getInnerElements();
}
