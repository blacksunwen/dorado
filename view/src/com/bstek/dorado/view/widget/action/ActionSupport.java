package com.bstek.dorado.view.widget.action;

import com.bstek.dorado.annotation.ViewAttribute;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-1
 */
public interface ActionSupport {
	@ViewAttribute(referenceComponentName = "Action")
	String getAction();

	void setAction(String action);
}
