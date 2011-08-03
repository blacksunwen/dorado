package com.bstek.dorado.ofc.element;

import java.util.List;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.ofc.Element;
import com.bstek.dorado.ofc.Key;
import com.bstek.dorado.ofc.binding.StackBindingConfig;

@ViewObject(prototype = "dorado.widget.ofc.StackedColumn", shortTypeName = "ofc.StackedColumn")
@XmlNode(nodeName = "StackedColumn")
public class StackedColumn extends Element{
	private List<Key> keys;
	private StackBindingConfig bindingConfig;
	/**
	 * @return the keys
	 */
	@ViewAttribute
	@XmlSubNode
	public List<Key> getKeys() {
		return keys;
	}
	/**
	 * @param keys the keys to set
	 */
	public void setKeys(List<Key> keys) {
		this.keys = keys;
	}
	/**
	 * @return the bindingConfig
	 */
	@ViewAttribute
	@XmlProperty(composite = true)
	public StackBindingConfig getBindingConfig() {
		return bindingConfig;
	}
	/**
	 * @param bindingConfig the bindingConfig to set
	 */
	public void setBindingConfig(StackBindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}
	
	
}
