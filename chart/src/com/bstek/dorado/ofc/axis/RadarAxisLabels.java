package com.bstek.dorado.ofc.axis;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

@XmlNode(nodeName = "RadarAxisLabels")
public class RadarAxisLabels {
	private String color;
	private List<AxisLabel> labels;
	private Double steps;
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
	 * @return the labels
	 */
	@ViewAttribute
	@XmlSubNode(path = "#self")
	public List<AxisLabel> getLabels() {
		return labels;
	}
	/**
	 * @param labels the labels to set
	 */
	public void setLabels(List<AxisLabel> labels) {
		this.labels = labels;
	}
	/**
	 * @return the steps
	 */
	public Double getSteps() {
		return steps;
	}
	/**
	 * @param steps the steps to set
	 */
	public void setSteps(Double steps) {
		this.steps = steps;
	}
	
	
	
}
