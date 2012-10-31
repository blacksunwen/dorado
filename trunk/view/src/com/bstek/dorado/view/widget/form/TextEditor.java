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
package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.property.Mapping;
import com.bstek.dorado.view.annotation.Widget;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-22
 */
@Widget(name = "TextEditor", category = "Form", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.TextEditor",
		shortTypeName = "TextEditor")
public class TextEditor extends AbstractTextEditor {
	@Deprecated
	private boolean password;

	private Mapping mapping;
	private DataType dataType;
	private String typeFormat;
	private String displayFormat;

	@XmlProperty(composite = true)
	@ClientProperty(outputter = "spring:dorado.mappingPropertyOutputter")
	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}

	@XmlProperty(parser = "spring:dorado.dataTypePropertyParser")
	@ClientProperty
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	@Deprecated
	@IdeProperty(visible = false)
	public boolean isPassword() {
		return password;
	}

	@Deprecated
	public void setPassword(boolean password) {
		this.password = password;
	}

	public String getTypeFormat() {
		return typeFormat;
	}

	public void setTypeFormat(String typeFormat) {
		this.typeFormat = typeFormat;
	}

	public String getDisplayFormat() {
		return displayFormat;
	}

	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}
}
