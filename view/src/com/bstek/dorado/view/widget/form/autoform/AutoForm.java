package com.bstek.dorado.view.widget.form.autoform;

import java.util.List;
import java.util.Properties;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.view.widget.Align;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementList;
import com.bstek.dorado.view.widget.form.FormConfig;
import com.bstek.dorado.view.widget.form.FormElementHintPosition;
import com.bstek.dorado.view.widget.form.FormElementLabelPosition;
import com.bstek.dorado.view.widget.form.FormElementType;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-9-3
 */
@Widget(name = "AutoForm", category = "Form", dependsPackage = "base-widget")
@ViewObject(prototype = "dorado.widget.AutoForm", shortTypeName = "AutoForm")
@XmlNode(nodeName = "AutoForm")
public class AutoForm extends Control implements FormConfig {
	private String formProfile;
	private String dataSet;
	private String dataPath;
	private EntityDataType dataType;
	private String cols;
	private int rowHeight;
	private int colPadding;
	private int rowPadding;
	private boolean stretchWidth;
	private int padding;
	private boolean autoCreateElements;
	private List<Control> elements = new InnerElementList<Control>(this);

	private String width;
	private String height;
	private String className;
	private String exClassName;
	private FormElementType type = FormElementType.text;
	private String trigger;
	private String labelSeparator;
	private boolean showLabel = true;
	private int labelWidth;
	private int labelSpacing;
	private FormElementLabelPosition labelPosition = FormElementLabelPosition.left;
	private Align labelAlign = Align.left;
	private int editorWidth;
	private Properties editorConfig;
	private boolean showHint = true;
	private int hintWidth;
	private int hintSpacing;
	private boolean showHintMessage;
	private FormElementHintPosition hintPosition;
	private boolean readOnly;
	private boolean createOwnEntity = true;

	@ViewAttribute(referenceComponentName = "FormProfile")
	public String getFormProfile() {
		return formProfile;
	}

	public void setFormProfile(String formProfile) {
		this.formProfile = formProfile;
	}

	@ViewAttribute(referenceComponentName = "DataSet")
	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	@XmlProperty(parser = "dorado.dataTypePropertyParser")
	@ViewAttribute(outputter = "dorado.dataTypePropertyOutputter")
	public EntityDataType getDataType() {
		return dataType;
	}

	public void setDataType(EntityDataType dataType) {
		this.dataType = dataType;
	}

	public String getCols() {
		return cols;
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	public int getColPadding() {
		return colPadding;
	}

	public void setColPadding(int colPadding) {
		this.colPadding = colPadding;
	}

	public int getRowPadding() {
		return rowPadding;
	}

	public void setRowPadding(int rowPadding) {
		this.rowPadding = rowPadding;
	}

	public boolean isStretchWidth() {
		return stretchWidth;
	}

	public void setStretchWidth(boolean stretchWidth) {
		this.stretchWidth = stretchWidth;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public boolean isAutoCreateElements() {
		return autoCreateElements;
	}

	public void setAutoCreateElements(boolean autoCreateElements) {
		this.autoCreateElements = autoCreateElements;
	}

	public void addElement(Control element) {
		elements.add(element);
	}

	@ViewAttribute
	@XmlSubNode(parser = "dorado.AutoForm.ElementsParser", path = "#self")
	public List<Control> getElements() {
		return elements;
	}

	@Override
	public String getWidth() {
		return width;
	}

	@Override
	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	public String getHeight() {
		return height;
	}

	@Override
	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String getExClassName() {
		return exClassName;
	}

	@Override
	public void setExClassName(String exClassName) {
		this.exClassName = exClassName;
	}

	@ViewAttribute(defaultValue = "text")
	public FormElementType getType() {
		return type;
	}

	public void setType(FormElementType type) {
		this.type = type;
	}

	@ViewAttribute(referenceComponentName = "Trigger", enumValues = "triggerClear,autoMappingDropDown1,autoMappingDropDown2,defaultDateDropDown")
	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getLabelSeparator() {
		return labelSeparator;
	}

	public void setLabelSeparator(String labelSeparator) {
		this.labelSeparator = labelSeparator;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isShowLabel() {
		return showLabel;
	}

	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	public int getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(int labelWidth) {
		this.labelWidth = labelWidth;
	}

	public int getLabelSpacing() {
		return labelSpacing;
	}

	public void setLabelSpacing(int labelSpacing) {
		this.labelSpacing = labelSpacing;
	}

	@ViewAttribute(defaultValue = "left")
	public FormElementLabelPosition getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(FormElementLabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	@ViewAttribute(defaultValue = "left")
	public Align getLabelAlign() {
		return labelAlign;
	}

	public void setLabelAlign(Align labelAlign) {
		this.labelAlign = labelAlign;
	}

	public int getEditorWidth() {
		return editorWidth;
	}

	public void setEditorWidth(int editorWidth) {
		this.editorWidth = editorWidth;
	}

	public Properties getEditorConfig() {
		return editorConfig;
	}

	public void setEditorConfig(Properties editorConfig) {
		this.editorConfig = editorConfig;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isShowHint() {
		return showHint;
	}

	public void setShowHint(boolean showHint) {
		this.showHint = showHint;
	}

	public int getHintWidth() {
		return hintWidth;
	}

	public void setHintWidth(int hintWidth) {
		this.hintWidth = hintWidth;
	}

	public int getHintSpacing() {
		return hintSpacing;
	}

	public void setHintSpacing(int hintSpacing) {
		this.hintSpacing = hintSpacing;
	}

	public boolean isShowHintMessage() {
		return showHintMessage;
	}

	public void setShowHintMessage(boolean showHintMessage) {
		this.showHintMessage = showHintMessage;
	}

	public FormElementHintPosition getHintPosition() {
		return hintPosition;
	}

	public void setHintPosition(FormElementHintPosition hintPosition) {
		this.hintPosition = hintPosition;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@ViewAttribute(defaultValue = "true")
	public boolean isCreateOwnEntity() {
		return createOwnEntity;
	}

	public void setCreateOwnEntity(boolean createOwnEntity) {
		this.createOwnEntity = createOwnEntity;
	}

}
