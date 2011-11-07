/**
 * 
 */
package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.base.SimpleIconButton;

/**
 * @author bean
 * 
 */
@Widget(name = "QuickButton", category = "Desktop", dependsPackage = "desktop")
@XmlNode(nodeName = "QuickButton")
@ViewObject(prototype = "dorado.widget.desktop.QuickButton", shortTypeName = "desktop.QuickButton")
public class QuickButton extends SimpleIconButton {
	private String appId;

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
