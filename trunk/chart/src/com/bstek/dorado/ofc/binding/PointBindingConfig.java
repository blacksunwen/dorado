package com.bstek.dorado.ofc.binding;

public class PointBindingConfig extends ChartBindingConfig{
	private String xProperty;
	private String yProperty;
	public String getxProperty() {
		return xProperty;
	}
	public void setxProperty(String xProperty) {
		this.xProperty = xProperty;
	}
	public String getyProperty() {
		return yProperty;
	}
	public void setyProperty(String yProperty) {
		this.yProperty = yProperty;
	}
}
