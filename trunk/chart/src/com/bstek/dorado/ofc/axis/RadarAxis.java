package com.bstek.dorado.ofc.axis;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;

public class RadarAxis extends AbstractAxis {
	private RadarAxisLabels labels;
	private RadarAxisSpokeLabels spokeLabels;
	private String spokeLabelsShortcut;

	/**
	 * @return the labels
	 */
	@XmlSubNode
	@ClientProperty
	public RadarAxisLabels getLabels() {
		return labels;
	}

	/**
	 * @param labels
	 *            the labels to set
	 */
	public void setLabels(RadarAxisLabels labels) {
		this.labels = labels;
	}

	/**
	 * @return the spokeLabels
	 */
	@XmlSubNode
	@ClientProperty
	public RadarAxisSpokeLabels getSpokeLabels() {
		return spokeLabels;
	}

	/**
	 * @param spokeLabels
	 *            the spokeLabels to set
	 */
	public void setSpokeLabels(RadarAxisSpokeLabels spokeLabels) {
		this.spokeLabels = spokeLabels;
	}

	/**
	 * @return the spokeLabelsShortcut
	 */
	public String getSpokeLabelsShortcut() {
		return spokeLabelsShortcut;
	}

	/**
	 * @param spokeLabelsShortcut
	 *            the spokeLabelsShortcut to set
	 */
	public void setSpokeLabelsShortcut(String spokeLabelsShortcut) {
		this.spokeLabelsShortcut = spokeLabelsShortcut;
	}
}
