package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ClientObject;

@ClientObject(prototype = "dorado.widget.ofc.Area", shortTypeName = "ofc.Area")
public class Area extends Line {
	private Double fillAlpha;
	private String fillColor;
	private boolean loop;

	/**
	 * @return the fillAlpha
	 */
	public Double getFillAlpha() {
		return fillAlpha;
	}

	/**
	 * @param fillAlpha
	 *            the fillAlpha to set
	 */
	public void setFillAlpha(Double fillAlpha) {
		this.fillAlpha = fillAlpha;
	}

	/**
	 * @return the fillColor
	 */
	public String getFillColor() {
		return fillColor;
	}

	/**
	 * @param fillColor
	 *            the fillColor to set
	 */
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * @return the loop
	 */
	public boolean isLoop() {
		return loop;
	}

	/**
	 * @param loop
	 *            the loop to set
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

}
