package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.Point;

@ClientObject(prototype = "dorado.widget.ofc.Arrow",
		shortTypeName = "ofc.Arrow")
public class Arrow extends Element {
	private Point start;
	private Point end;
	private String color;
	private Double alpha;
	private Double barbLength;

	/**
	 * @return the start
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public Point getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(Point start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public Point getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(Point end) {
		this.end = end;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha
	 *            the alpha to set
	 */
	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return the barbLength
	 */
	public Double getBarbLength() {
		return barbLength;
	}

	/**
	 * @param barbLength
	 *            the barbLength to set
	 */
	public void setBarbLength(Double barbLength) {
		this.barbLength = barbLength;
	}

}
