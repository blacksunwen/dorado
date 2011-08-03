package com.bstek.dorado.ofc.model;

public class Candle extends Column {
	private Double high;
	private Double low;
	private String negativeColor;
	
	public Double getHigh() {
		return high;
	}
	public void setHigh(Double high) {
		this.high = high;
	}
	public Double getLow() {
		return low;
	}
	public void setLow(Double low) {
		this.low = low;
	}
	public String getNegativeColor() {
		return negativeColor;
	}
	public void setNegativeColor(String negativeColor) {
		this.negativeColor = negativeColor;
	}
	
}
