package com.bstek.dorado.ofc.binding;

public class SketchColumnBindingConfig extends ColumnBindingConfig{
	private String valueProperty;
	private String topProperty;
	private String bottomProperty;
	private String offsetProperty;
	private String outlineColorProperty;
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
	public String getOffsetProperty() {
		return offsetProperty;
	}
	public void setOffsetProperty(String offsetProperty) {
		this.offsetProperty = offsetProperty;
	}
	public String getOutlineColorProperty() {
		return outlineColorProperty;
	}
	public void setOutlineColorProperty(String outlineColorProperty) {
		this.outlineColorProperty = outlineColorProperty;
	}
}
