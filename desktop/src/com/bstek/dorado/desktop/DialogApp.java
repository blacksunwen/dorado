package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.widget.InnerElementReference;
import com.bstek.dorado.view.widget.base.Dialog;

@XmlNode(nodeName = "DialogApp")
@ViewObject(prototype = "dorado.widget.desktop.ControlApp", shortTypeName = "Control")
public class DialogApp extends AbstractControlApp {
	private InnerElementReference<Dialog> controlRef = new InnerElementReference<Dialog>(
			this);
	private boolean showTaskButton = true;
	
	/**
	 * @return
	 */
	@ViewAttribute
	@XmlSubNode(path = "*", fixed = true)
	public Dialog getControl() {
		return controlRef.get();
	}

	/**
	 * @param control
	 */
	public void setControl(Dialog control) {
		controlRef.set(control);
	}

	/**
	 * @return the showTaskButton
	 */
	@ViewAttribute(defaultValue = "true")
	public boolean isShowTaskButton() {
		return showTaskButton;
	}

	/**
	 * @param showTaskButton the showTaskButton to set
	 */
	public void setShowTaskButton(boolean showTaskButton) {
		this.showTaskButton = showTaskButton;
	}
	
	
}
