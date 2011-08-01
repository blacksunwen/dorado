package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.property.Mapping;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
public abstract class AbstractTextEditor extends AbstractTextBox {
	private DataType dataType;
	private boolean editable = true;
	private String blankText;
	private boolean selectTextOnFocus = true;
	private Mapping mapping;

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getBlankText() {
		return blankText;
	}

	public void setBlankText(String blankText) {
		this.blankText = blankText;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isSelectTextOnFocus() {
		return selectTextOnFocus;
	}

	public void setSelectTextOnFocus(boolean selectTextOnFocus) {
		this.selectTextOnFocus = selectTextOnFocus;
	}

	@XmlProperty(composite = true)
	@ViewAttribute(outputter = "dorado.mappingPropertyOutputter")
	public Mapping getMapping() {
		return mapping;
	}

	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}
}
