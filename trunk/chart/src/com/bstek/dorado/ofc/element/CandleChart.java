package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.binding.CandleBindingConfig;

@ClientObject(prototype = "dorado.widget.ofc.Candle",
		shortTypeName = "ofc.Candle")
public class CandleChart extends Column {
	private String negativeColor;
	private CandleBindingConfig bindingConfig;

	/**
	 * @return the bindingConfig
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public CandleBindingConfig getBindingConfig() {
		return bindingConfig;
	}

	/**
	 * @param bindingConfig
	 *            the bindingConfig to set
	 */
	public void setBindingConfig(CandleBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}

	/**
	 * @return the negativeColor
	 */
	public String getNegativeColor() {
		return negativeColor;
	}

	/**
	 * @param negativeColor
	 *            the negativeColor to set
	 */
	public void setNegativeColor(String negativeColor) {
		this.negativeColor = negativeColor;
	}

}
