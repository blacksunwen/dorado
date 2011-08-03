package com.bstek.dorado.ofc.binding;

public class ColumnBindingConfig extends ChartBindingConfig{
	private String valueProperty;
	private String topProperty;
	private String bottomProperty;
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
