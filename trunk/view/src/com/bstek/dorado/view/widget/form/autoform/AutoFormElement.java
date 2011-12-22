package com.bstek.dorado.view.widget.form.autoform;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.IdeObject;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.form.FormElement;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-3
 */
@Widget(name = "AutoFormElement", category = "AutoFormElement",
		dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.autoform.AutoFormElement",
		shortTypeName = "Default")
@IdeObject(labelProperty = "id,name,property")
public class AutoFormElement extends FormElement {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
