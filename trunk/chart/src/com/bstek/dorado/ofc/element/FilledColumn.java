package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;

@ViewObject(prototype = "dorado.widget.ofc.FilledColumn", shortTypeName = "ofc.FilledColumn")
@XmlNode(nodeName = "FilledColumn")
public class FilledColumn extends Column{
	private String outlineColor;
	
	/**
	 * @return the outlineColor
	 */
	public String getOutlineColor() {
		return outlineColor;
	}

	/**
	 * @param outlineColor the outlineColor to set
	 */
	public void setOutlineColor(String outlineColor) {
		this.outlineColor = outlineColor;
	}
	
	
}
