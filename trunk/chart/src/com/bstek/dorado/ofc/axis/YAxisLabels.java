package com.bstek.dorado.ofc.axis;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

@XmlNode(nodeName = "Labels")
@ClientObject
public class YAxisLabels {
	private String color;
	private int size;
	private String text;
	private boolean visible = true;
	private int steps;
	private List<AxisLabel> labels;

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	@XmlSubNode
	@ClientProperty
	public List<AxisLabel> getLabels() {
		return labels;
	}

	public void setLabels(List<AxisLabel> labels) {
		this.labels = labels;
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
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the visible
	 */
	@ClientProperty(escapeValue = "true")
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
