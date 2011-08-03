package com.bstek.dorado.ofc.binding;

public class BarBindingConfig extends ChartBindingConfig{
	private String valueProperty;
	private String leftProperty;
	private String rightProperty;
	private String colorProperty;
	private String toolTipProperty;
	
	public String getValueProperty() {
		return valueProperty;
	}
	public void setValueProperty(String valueProperty) {
		this.valueProperty = valueProperty;
	}
	public String getLeftProperty() {
		return leftProperty;
	}
	public void setLeftProperty(String leftProperty) {
		this.leftProperty = leftProperty;
	}
	public String getRightProperty() {
		return rightProperty;
	}
	public void setRightProperty(String rightProperty) {
		this.rightProperty = rightProperty;
	}
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
	
}
