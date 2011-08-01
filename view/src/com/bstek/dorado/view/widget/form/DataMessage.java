package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.datacontrol.AbstractPropertyDataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-5-26
 */
@Widget(name = "DataMessage", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.DataMessage", shortTypeName = "DataMessage")
@XmlNode(nodeName = "DataMessage")
public class DataMessage extends AbstractPropertyDataControl {
	private boolean showIconOnly;
	private boolean showMultiMessage;

	public boolean isShowIconOnly() {
		return showIconOnly;
	}

	public void setShowIconOnly(boolean showIconOnly) {
		this.showIconOnly = showIconOnly;
	}

	public boolean isShowMultiMessage() {
		return showMultiMessage;
	}

	public void setShowMultiMessage(boolean showMultiMessage) {
		this.showMultiMessage = showMultiMessage;
	}
}
