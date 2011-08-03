package com.bstek.dorado.ofc.axis;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlSubNode;

public class YAxis extends AbstractAxis {
	private YAxisLabels labels;
	private Integer tickLength;
	
	private Integer offset;
	private Integer zDepth3D;
	
	/**
	 * @return the labels
	 */
	@ViewAttribute
	@XmlSubNode
	public YAxisLabels getLabels() {
		return labels;
	}
	/**
	 * @param labels the labels to set
	 */
	public void setLabels(YAxisLabels labels) {
		this.labels = labels;
	}
	
	/**
	 * @return the tickLength
	 */
	public Integer getTickLength() {
		return tickLength;
	}
	/**
	 * @param tickLength the tickLength to set
	 */
	public void setTickLength(Integer tickLength) {
		this.tickLength = tickLength;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getzDepth3D() {
		return zDepth3D;
	}
	public void setzDepth3D(Integer zDepth3D) {
		this.zDepth3D = zDepth3D;
	}
	
}
