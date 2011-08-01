package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-9
 */
@Widget(name = "Link", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.Link", shortTypeName = "Link")
@XmlNode(nodeName = "Link")
public class Link extends Label {
	private String href;
	private String target;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
