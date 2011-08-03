package com.bstek.dorado.ofc.element;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.ofc.binding.SketchColumnBindingConfig;

@ViewObject(prototype = "dorado.widget.ofc.SketchColumn", shortTypeName = "ofc.SketchColumn")
@XmlNode(nodeName = "SketchColumn")
public class SketchColumn extends FilledColumn{
	private Integer offset;
	private SketchColumnBindingConfig bindingConfig;
	/**
	 * @return the offset
	 */
	public Integer getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	/**
	 * @return the bindingConfig
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public SketchColumnBindingConfig getBindingConfig() {
		return bindingConfig;
	}
	/**
	 * @param bindingConfig the bindingConfig to set
	 */
	public void setBindingConfig(SketchColumnBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}
	
	
}
