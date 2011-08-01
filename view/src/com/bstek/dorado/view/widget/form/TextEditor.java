package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.type.DataType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2009-9-22
 */
@Widget(name = "TextEditor", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.TextEditor", shortTypeName = "TextEditor")
@XmlNode(nodeName = "TextEditor")
public class TextEditor extends AbstractTextEditor {
	private DataType dataType;
	private boolean password;
	private String typeFormat;
	private String displayFormat;
	private boolean required;
	private int minLength;
	private int maxLength;

	@Override
	@XmlProperty(parser = "dorado.dataTypePropertyParser")
	@ViewAttribute(outputter = "dorado.dataTypePropertyOutputter")
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public boolean isPassword() {
		return password;
	}

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

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
