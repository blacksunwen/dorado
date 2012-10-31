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
package com.bstek.dorado.data.type.property;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.XmlNode;

/**
 * 基本属性。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apirl 17, 2007
 */
@XmlNode(
		nodeName = "PropertyDef",
		parser = "spring:dorado.propertyDefParser",
		definitionType = "com.bstek.dorado.data.config.definition.PropertyDefDefinition")
@ClientObject(prototype = "dorado.BasePropertyDef", shortTypeName = "Default")
public class BasePropertyDef extends PropertyDefSupport {
	private String propertyPath;

	public BasePropertyDef() {
	}

	public BasePropertyDef(String name) {
		setName(name);
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}
}
