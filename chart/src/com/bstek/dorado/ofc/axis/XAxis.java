package com.bstek.dorado.ofc.axis;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlSubNode;

public class XAxis extends AbstractAxis {
	private XAxisLabels labels;
	private int tickHeight;
	private int offset;
	private int zDepth3D;

	/**
	 * @return the labels
	 */
	@XmlSubNode
	@ClientProperty
	public XAxisLabels getLabels() {
		return labels;
	}

	/**
	 * @param labels
	 *            the labels to set
	 */
	public void setLabels(XAxisLabels labels) {
		this.labels = labels;
	}

	/**
	 * @return the tickHeight
	 */
	public int getTickHeight() {
		return tickHeight;
	}

	/**
	 * @param tickHeight
	 *            the tickHeight to set
	 */
	public void setTickHeight(int tickHeight) {
		this.tickHeight = tickHeight;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getzDepth3D() {
		return zDepth3D;
	}

	public void setzDepth3D(int zDepth3D) {
		this.zDepth3D = zDepth3D;
	}

}
