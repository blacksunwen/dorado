package com.bstek.dorado.ofc.axis;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.XmlNode;

@XmlNode
@ClientObject
public class AbstractAxis {
	private String color;
	private String gridColor;
	private Double max;
	private Double min;
	private Double steps;
	private int stroke;

	private String labelsShortcut;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getGridColor() {
		return gridColor;
	}

	public void setGridColor(String gridColor) {
		this.gridColor = gridColor;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getSteps() {
		return steps;
	}

	public void setSteps(Double steps) {
		this.steps = steps;
	}

	public int getStroke() {
		return stroke;
	}

	public void setStroke(int stroke) {
		this.stroke = stroke;
	}

	/**
	 * @return the labelsShortcut
	 */
	public String getLabelsShortcut() {
		return labelsShortcut;
	}

	/**
	 * @param labelsShortcut
	 *            the labelsShortcut to set
	 */
	public void setLabelsShortcut(String labelsShortcut) {
		this.labelsShortcut = labelsShortcut;
	}

}
