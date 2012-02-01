package com.bstek.dorado.ofc;

import java.util.List;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.ofc.axis.RadarAxis;
import com.bstek.dorado.ofc.axis.XAxis;
import com.bstek.dorado.ofc.axis.YAxis;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Control;

@Widget(name = "OpenFlashChart", category = "Advance", dependsPackage = "chart")
@ClientObject(prototype = "dorado.widget.ofc.OpenFlashChart",
		shortTypeName = "ofc.OpenFlashChart")
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
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Elements", fixed = true))
	@ClientProperty
	public List<Element> getElements() {
		return elements;
	}

	/**
	 * @param elements
	 *            the elements to set
	 */
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	/**
	 * @return the legend
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public Legend getLegend() {
		return legend;
	}

	/**
	 * @param legend
	 *            the legend to set
	 */
	public void setLegend(Legend legend) {
		this.legend = legend;
	}

	/**
	 * @return the radarAxis
	 */
	@XmlSubNode
	@ClientProperty
	public RadarAxis getRadarAxis() {
		return radarAxis;
	}

	/**
	 * @param radarAxis
	 *            the radarAxis to set
	 */
	public void setRadarAxis(RadarAxis radarAxis) {
		this.radarAxis = radarAxis;
	}

	/**
	 * @return the title
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public Text getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(Text title) {
		this.title = title;
	}

	/**
	 * @return the xAxis
	 */
	@XmlSubNode
	@ClientProperty
	public XAxis getxAxis() {
		return xAxis;
	}

	/**
	 * @param xAxis
	 *            the xAxis to set
	 */
	public void setxAxis(XAxis xAxis) {
		this.xAxis = xAxis;
	}

	/**
	 * @return the yAxis
	 */
	@XmlSubNode(nodeName = "YAxis")
	@ClientProperty
	public YAxis getyAxis() {
		return yAxis;
	}

	/**
	 * @param yAxis
	 *            the yAxis to set
	 */
	public void setyAxis(YAxis yAxis) {
		this.yAxis = yAxis;
	}

	/**
	 * @return the xLegend
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public Text getxLegend() {
		return xLegend;
	}

	/**
	 * @param xLegend
	 *            the xLegend to set
	 */
	public void setxLegend(Text xLegend) {
		this.xLegend = xLegend;
	}

	/**
	 * @return the yLegend
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public Text getyLegend() {
		return yLegend;
	}

	/**
	 * @param yLegend
	 *            the yLegend to set
	 */
	public void setyLegend(Text yLegend) {
		this.yLegend = yLegend;
	}

	/**
	 * @return the yAxisRight
	 */
	@XmlSubNode(nodeName = "YAxisRight")
	@ClientProperty
	public YAxis getyAxisRight() {
		return yAxisRight;
	}

	/**
	 * @param yAxisRight
	 *            the yAxisRight to set
	 */
	public void setyAxisRight(YAxis yAxisRight) {
		this.yAxisRight = yAxisRight;
	}

	/**
	 * @return the yLegendRight
	 */
	@XmlProperty(composite = true)
	@ClientProperty
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
	@XmlProperty(composite = true)
	@ClientProperty
	public ToolTip getToolTip() {
		return toolTip;
	}

	/**
	 * @param toolTip
	 *            the toolTip to set
	 */
	public void setToolTip(ToolTip toolTip) {
		this.toolTip = toolTip;
	}

}
