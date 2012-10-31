/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
