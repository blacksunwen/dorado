package com.bstek.dorado.view.widget;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-6-19
 */
@Widget(name = "SubViewHolder", category = "General", dependsPackage = "widget")
@ViewObject(prototype = "dorado.widget.SubViewHolder", shortTypeName = "SubViewHolder")
@XmlNode(nodeName = "SubViewHolder")
public class SubViewHolder extends Control implements HtmlElement {
	private String subView;

	@ViewAttribute(outputter = "dorado.subViewPropertyOutputter")
	public String getSubView() {
		return subView;
	}

	public void setSubView(String subView) {
		this.subView = subView;
	}
}
