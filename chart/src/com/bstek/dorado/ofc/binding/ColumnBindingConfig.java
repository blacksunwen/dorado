package com.bstek.dorado.ofc.binding;

public class ColumnBindingConfig extends ChartBindingConfig{
	private String valueProperty;
	private String topProperty;
	private String bottomProperty;
	private String colorProperty;
	private String toolTipProperty;
	
	public String getColorProperty() {
		return colorProperty;
	}
	public void setColorProperty(String colorProperty) {
		this.colorProperty = colorProperty;
	}
	public String getToolTipProperty() {
		return toolTipProperty;
	}
	public void setToolTipProperty(String toolTipProperty) {
		this.toolTipProperty = toolTipProperty;
	}
	public String getValueProperty() {
		return valueProperty;
	}
	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}
	public String getTopProperty() {
		return topProperty;
	}
	public void setTopProperty(String topProperty) {
		this.topProperty = topProperty;
	}
	public String getBottomProperty() {
		return bottomProperty;
	}
	public void setBottomProperty(String bottomProperty) {
		this.bottomProperty = bottomProperty;
	}
	
	
}
