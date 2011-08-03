package com.bstek.dorado.ofc;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.ofc.axis.RadarAxis;
import com.bstek.dorado.ofc.axis.XAxis;
import com.bstek.dorado.ofc.axis.YAxis;
import com.bstek.dorado.view.widget.Control;

@Widget(name = "OpenFlashCart", category = "Advance", dependsPackage = "chart")
@ViewObject(prototype = "dorado.widget.ofc.OpenFlashChart", shortTypeName = "ofc.OpenFlashChart")
@XmlNode(nodeName = "OpenFlashCart")
public class OpenFlashChart extends Control {
	private String backgroundColor;
	private List<Element> elements;
	private Legend legend;
	private RadarAxis radarAxis;
	private Text title;
	private XAxis xAxis;
	private YAxis yAxis;
	private Text xLegend;
	private Text yLegend;
	private YAxis yAxisRight;
	private Text yLegendRight;
	private ToolTip toolTip;

	/**
	 * @return the backgroundColor
	 */
	public String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *            the backgroundColor to set
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * @return the elements
	 */
	@ViewAttribute
	@XmlSubNode( parser = "dorado.OpenFlashChart.ElementsParser", fixed = true)
	public List<Element> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	/**
	 * @return the legend
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public Legend getLegend() {
		return legend;
	}

	/**
	 * @param legend the legend to set
	 */
	public void setLegend(Legend legend) {
		this.legend = legend;
	}

	/**
	 * @return the radarAxis
	 */
	@ViewAttribute
	@XmlSubNode
	public RadarAxis getRadarAxis() {
		return radarAxis;
	}

	/**
	 * @param radarAxis the radarAxis to set
	 */
	public void setRadarAxis(RadarAxis radarAxis) {
		this.radarAxis = radarAxis;
	}

	/**
	 * @return the title
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public Text getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(Text title) {
		this.title = title;
	}

	/**
	 * @return the xAxis
	 */
	@ViewAttribute
	@XmlSubNode
	public XAxis getxAxis() {
		return xAxis;
	}

	/**
	 * @param xAxis the xAxis to set
	 */
	public void setxAxis(XAxis xAxis) {
		this.xAxis = xAxis;
	}

	/**
	 * @return the yAxis
	 */
	@ViewAttribute
	@XmlSubNode
	public YAxis getyAxis() {
		return yAxis;
	}

	/**
	 * @param yAxis the yAxis to set
	 */
	public void setyAxis(YAxis yAxis) {
		this.yAxis = yAxis;
	}

	/**
	 * @return the xLegend
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public Text getxLegend() {
		return xLegend;
	}

	/**
	 * @param xLegend the xLegend to set
	 */
	public void setxLegend(Text xLegend) {
		this.xLegend = xLegend;
	}

	/**
	 * @return the yLegend
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public Text getyLegend() {
		return yLegend;
	}

	/**
	 * @param yLegend the yLegend to set
	 */
	public void setyLegend(Text yLegend) {
		this.yLegend = yLegend;
	}

	/**
	 * @return the yAxisRight
	 */
	@ViewAttribute
	@XmlSubNode
	public YAxis getyAxisRight() {
		return yAxisRight;
	}

	/**
	 * @param yAxisRight the yAxisRight to set
	 */
	public void setyAxisRight(YAxis yAxisRight) {
		this.yAxisRight = yAxisRight;
	}

	/**
	 * @return the yLegendRight
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public Text getyLegendRight() {
		return yLegendRight;
	}

	/**
	 * @param yLegendRight
	 *            the yLegendRight to set
	 */
	public void setyLegendRight(Text yLegendRight) {
		this.yLegendRight = yLegendRight;
	}

	/**
	 * @return the toolTip
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public ToolTip getToolTip() {
		return toolTip;
	}

	/**
	 * @param toolTip the toolTip to set
	 */
	public void setToolTip(ToolTip toolTip) {
		this.toolTip = toolTip;
	}
	
	
}
