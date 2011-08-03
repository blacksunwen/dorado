package com.bstek.dorado.ofc.element;

public class ShowAnimation {
	public enum ShowAnimationType{
		popUp,explode,midSlide,drop,fadeIn,shrinkIn
	}
	
	private ShowAnimationType type;
	private Double cascade;
	private Double delay;
	/**
	 * @return the type
	 */
	public ShowAnimationType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(ShowAnimationType type) {
		this.type = type;
	}
	/**
	 * @return the cascade
	 */
	public Double getCascade() {
		return cascade;
	}
	/**
	 * @param cascade the cascade to set
	 */
	public void setCascade(Double cascade) {
		this.cascade = cascade;
	}
	/**
	 * @return the delay
	 */
	public Double getDelay() {
		return delay;
	}
	/**
	 * @param delay the delay to set
	 */
	public void setDelay(Double delay) {
		this.delay = delay;
	}
	
	
}
