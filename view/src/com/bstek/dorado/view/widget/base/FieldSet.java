package com.bstek.dorado.view.widget.base;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-6
 */
@Widget(name = "FieldSet", category = "General", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.FieldSet", shortTypeName = "FieldSet")
@XmlNode(nodeName = "FieldSet")
public class FieldSet extends AbstractPanel {
	public FieldSet() {
		this.setCollapseable(true);
	}

	@ViewAttribute(defaultValue = "true")
	@Override
	public void setCollapseable(boolean collapseable) {
		super.setCollapseable(collapseable);
	}
}
