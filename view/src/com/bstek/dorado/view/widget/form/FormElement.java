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
import com.bstek.dorado.view.widget.datacontrol.PropertyDataControl;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
@Widget(name = "FormElement", category = "Form", dependsPackage = "base-widget")
@ClientObject(prototype = "dorado.widget.FormElement",
		shortTypeName = "FormElement")
public class FormElement extends Control implements FormConfig,
		PropertyDataControl {
	@Deprecated
	private FormElementType type = FormElementType.text;
	@Deprecated
	private boolean editorTypeChanged;

	private String dataSet;
	private String dataPath;
	private String label;
	private String hint;
	private String property;
	private String trigger;
	private boolean editable = true;
	private String labelSeparator = ":";
	private boolean showLabel = true;
	private int labelWidth = 80;
	private int labelSpacing = 3;
	private FormElementLabelPosition labelPosition = FormElementLabelPosition.left;
	private Align labelAlign = Align.left;
	private int editorWidth;
	private boolean showHint = true;
	private int hintWidth = 22;
	private int hintSpacing = 3;
	private boolean showHintMessage;
	private FormElementHintPosition hintPosition = FormElementHintPosition.right;
	private boolean readOnly;
	private String formProfile;

	private String editorType = "TextEditor";
	private InnerElementReference<Control> editorRef = new InnerElementReference<Control>(
			this);

	@Deprecated
	@ClientProperty(ignored = true)
	@IdeProperty(visible = false)
	public FormElementType getType() {
		return type;
	}

	@Deprecated
	public void setType(FormElementType type) {
		this.type = type;
		if (!editorTypeChanged) {
			if (FormElementType.text.equals(type)) {
				editorType = "TextEditor";
			} else if (FormElementType.password.equals(type)) {
				editorType = "PasswordEditor";
			} else if (FormElementType.textArea.equals(type)) {
				editorType = "TextArea";
			} else if (FormElementType.checkBox.equals(type)) {
				editorType = "CheckBox";
			} else if (FormElementType.radioGroup.equals(type)) {
				editorType = "RadioGroup";
			} else if (type == null) {
				editorType = "TextEditor";
			}
		}
	}

	@ComponentReference("DataSet")
	@IdeProperty(highlight = 1)
	public String getDataSet() {
		return dataSet;
	}

	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}

	@IdeProperty(highlight = 1)
	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	@IdeProperty(highlight = 1)
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

	@IdeProperty(highlight = 1)
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@ComponentReference("Trigger")
	@IdeProperty(
			enumValues = "triggerClear,autoMappingDropDown1,autoMappingDropDown2,defaultDateDropDown,defaultDateTimeDropDown,defaultYearMonthDropDown")
	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	@ClientProperty(escapeValue = "true")
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@ClientProperty(escapeValue = ":")
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

	@ClientProperty(escapeValue = "80")
	public int getLabelWidth() {
		return labelWidth;
	}

	public void setLabelWidth(int labelWidth) {
		this.labelWidth = labelWidth;
	}

	@ClientProperty(escapeValue = "3")
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

	@ClientProperty(escapeValue = "22")
	public int getHintWidth() {
		return hintWidth;
	}

	public void setHintWidth(int hintWidth) {
		this.hintWidth = hintWidth;
	}

	@ClientProperty(escapeValue = "3")
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

	@ClientProperty(escapeValue = "right")
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

	@ClientProperty(escapeValue = "TextEditor")
	@IdeProperty(
			highlight = 1,
			enumValues = "TextEditor,PasswordEditor,TextArea,CheckBox,RadioGroup,DataLabel,NumberSpinner")
	public String getEditorType() {
		return editorType;
	}

	public void setEditorType(String editorType) {
		this.editorType = editorType;
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
