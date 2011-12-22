package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.view.annotation.ComponentReference;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.Align;
import com.bstek.dorado.view.widget.Control;
import com.bstek.dorado.view.widget.InnerElementReference;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "FormElement", category = "Form", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.FormElement",
		shortTypeName = "FormElement")
public class FormElement extends Control implements FormConfig {
	private FormElementType type = FormElementType.text;
	private String dataSet;
	private String dataPath;
	private String label;
	private String hint;
	private String property;
	private String trigger;
	private String labelSeparator;
	private boolean showLabel = true;
	private int labelWidth;
	private int labelSpacing;
	private FormElementLabelPosition labelPosition = FormElementLabelPosition.left;
	private Align labelAlign = Align.left;
	private int editorWidth;
	private boolean showHint = true;
	private int hintWidth;
	private int hintSpacing;
	private boolean showHintMessage;
	private FormElementHintPosition hintPosition;
	private boolean readOnly;
	private String formProfile;
	private InnerElementReference<Control> editorRef = new InnerElementReference<Control>(
			this);

	@ClientProperty(escapeValue = "text")
	public FormElementType getType() {
		return type;
	}

	public void setType(FormElementType type) {
		this.type = type;
	}

	@ComponentReference("DataSet")
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@ComponentReference("Trigger")
	@IdeProperty(
			enumValues = "triggerClear,autoMappingDropDown1,autoMappingDropDown2,defaultDateDropDown")
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

	@ClientProperty(escapeValue = "true")
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

	@ClientProperty(escapeValue = "left")
	public FormElementLabelPosition getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(FormElementLabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}

	@ClientProperty(escapeValue = "left")
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

	@ClientProperty(escapeValue = "true")
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

	@ComponentReference("FormProfile")
	public String getFormProfile() {
		return formProfile;
	}

	public void setFormProfile(String formProfile) {
		this.formProfile = formProfile;
	}

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Editor"))
	@ClientProperty
	public Control getEditor() {
		return editorRef.get();
	}

	public void setEditor(Control editor) {
		editorRef.set(editor);
	}

}
