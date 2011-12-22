/**
 * 
 */
package com.bstek.dorado.desktop;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.view.widget.base.SimpleIconButton;

/**
 * @author bean
 * 
 */
@ClientObject(prototype = "dorado.widget.desktop.QuickButton",
		shortTypeName = "desktop.QuickButton")
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
