package com.bstek.dorado.view.widget;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-6-19
 */
@Widget(name = "SubViewHolder", category = "General", dependsPackage = "widget")
@ClientObject(prototype = "dorado.widget.SubViewHolder",
		shortTypeName = "SubViewHolder")
public class SubViewHolder extends Control implements HtmlElement {
	private String subView;

	@ClientProperty(outputter = "spring:dorado.subViewPropertyOutputter")
	public String getSubView() {
		return subView;
	}

	public void setSubView(String subView) {
		this.subView = subView;
	}
}
