package com.bstek.dorado.view.widget.form.autoform;


import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.form.FormElement;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-3
 */
@Widget(name = "AutoForm.AutoFormElement", category = "AutoFormElement", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.autoform.AutoFormElement")
@XmlNode(nodeName = "AutoFormElement")
public class AutoFormElement extends FormElement {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
