package com.bstek.dorado.desktop;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;


@Widget(name = "DesktopCarousel", category = "Desktop", dependsPackage = "desktop")
@ViewObject(prototype = "dorado.widget.desktop.DesktopCarousel", shortTypeName = "desktop.DesktopCarousel")
@XmlNode(nodeName = "DesktopCarousel")
public class DesktopCarousel extends AbstractDesktop {
	
	/**
	 * @return the controls
	 */
	@ViewAttribute
	@XmlSubNode(path = "#self")
	public List<Desktop> getControls() {
		return controls;
	}

	/**
	 * @param controls the controls to set
	 */
	public void setControls(List<Desktop> controls) {
		this.controls = controls;
	}

	private List<Desktop> controls;
	
	
}
