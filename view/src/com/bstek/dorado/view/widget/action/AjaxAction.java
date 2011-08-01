package com.bstek.dorado.view.widget.action;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "AjaxAction", category = "Action", dependsPackage = "widget")
@ViewObject(prototype = "dorado.widget.AjaxAction", shortTypeName = "AjaxAction")
@XmlNode(nodeName = "AjaxAction")
public class AjaxAction extends Action {
	private boolean async = true;
	private String service;

	@Override
	@ViewAttribute(defaultValue = "true")
	public boolean isAsync() {
		return async;
	}

	@Override
	public void setAsync(boolean async) {
		this.async = async;
	}

	@ViewAttribute(outputter = "dorado.stringAliasPropertyOutputter")
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
}
