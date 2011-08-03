package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.Point;

@ViewObject(prototype = "dorado.widget.ofc.Arrow", shortTypeName = "ofc.Arrow")
@XmlNode(nodeName = "Arrow")
public class Arrow extends Element{
	private Point start;
	private Point end;
	private String color;
	private Double alpha;
	private Double barbLength;
	
	/**
	 * @return the start
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public Point getStart() {
		return start;
	}
	
	/**
	 * @param start the start to set
	 */
	public void setStart(Point start) {
		this.start = start;
	}
	
	/**
	 * @return the end
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public Point getEnd() {
		return end;
	}
	
	/**
	 * @param end the end to set
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
	 * @param color the color to set
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
	 * @param alpha the alpha to set
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
	 * @param barbLength the barbLength to set
	 */
	public void setBarbLength(Double barbLength) {
		this.barbLength = barbLength;
	}
	
}
