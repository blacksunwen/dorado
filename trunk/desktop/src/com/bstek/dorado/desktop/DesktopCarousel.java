package com.bstek.dorado.desktop;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;

@ClientObject(prototype = "dorado.widget.desktop.DesktopCarousel",
		shortTypeName = "desktop.DesktopCarousel")
public class DesktopCarousel extends AbstractDesktop {

	/**
	 * @return the controls
	 */
	@XmlSubNode
	@ClientProperty
	public List<Desktop> getControls() {
		return controls;
	}

	/**
	 * @param controls
	 *            the controls to set
	 */
	public void setControls(List<Desktop> controls) {
		this.controls = controls;
	}

	private List<Desktop> controls;

}
