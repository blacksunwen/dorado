package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientEvent;
import com.bstek.dorado.annotation.ClientEvents;
import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.view.widget.Control;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-11-5
 */
@ClientEvents( { @ClientEvent(name = "beforePost"),
		@ClientEvent(name = "onPost"), @ClientEvent(name = "onPostFailed") })
public class AbstractEditor extends Control {
	private boolean readOnly;
	private boolean supportsDirtyFlag = true;

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isSupportsDirtyFlag() {
		return supportsDirtyFlag;
	}

	public void setSupportsDirtyFlag(boolean supportsDirtyFlag) {
		this.supportsDirtyFlag = supportsDirtyFlag;
	}
}
