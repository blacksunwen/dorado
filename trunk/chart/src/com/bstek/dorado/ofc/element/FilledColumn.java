package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ClientObject;

@ClientObject(prototype = "dorado.widget.ofc.FilledColumn",
		shortTypeName = "ofc.FilledColumn")
public class FilledColumn extends Column {
	private String outlineColor;

	/**
	 * @return the outlineColor
	 */
	public String getOutlineColor() {
		return outlineColor;
	}

	/**
	 * @param outlineColor
	 *            the outlineColor to set
	 */
	public void setOutlineColor(String outlineColor) {
		this.outlineColor = outlineColor;
	}

}
