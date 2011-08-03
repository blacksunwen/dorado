package com.bstek.dorado.ofc.binding;

public class CandleBindingConfig extends ColumnBindingConfig {
	private String valueProperty;
	private String topProperty;
	private String bottomProperty;
	private String highProperty;
	private String lowProperty;
	private String negativeColorProperty;

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

	public String getHighProperty() {
		return highProperty;
	}

	public void setHighProperty(String highProperty) {
		this.highProperty = highProperty;
	}

	public String getLowProperty() {
		return lowProperty;
	}

	public void setLowProperty(String lowProperty) {
		this.lowProperty = lowProperty;
	}

	public String getNegativeColorProperty() {
		return negativeColorProperty;
	}

	public void setNegativeColorProperty(String negativeColorProperty) {
		this.negativeColorProperty = negativeColorProperty;
	}

}
