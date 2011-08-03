package com.bstek.dorado.ofc.axis;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlSubNode;

@XmlNode(nodeName = "RadarAxisSpokeLabels")
public class RadarAxisSpokeLabels {
	private String color;
	private List<AxisLabel> labels;
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
}
