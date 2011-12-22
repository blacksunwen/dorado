package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.binding.SketchColumnBindingConfig;

@ClientObject(prototype = "dorado.widget.ofc.SketchColumn",
		shortTypeName = "ofc.SketchColumn")
public class SketchColumn extends FilledColumn {
	private Integer offset;
	private SketchColumnBindingConfig bindingConfig;

	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * @return the bindingConfig
	 */
	@XmlProperty(composite = true)
	@ClientProperty
	public SketchColumnBindingConfig getBindingConfig() {
		return bindingConfig;
	}

	/**
	 * @param bindingConfig
	 *            the bindingConfig to set
	 */
	public void setBindingConfig(SketchColumnBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}

}
