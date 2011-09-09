package com.bstek.dorado.view.widget.form;

import com.bstek.dorado.view.widget.Align;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-8-10
 */
public interface FormConfig {
	String getWidth();

	void setWidth(String width);

	String getHeight();

	void setHeight(String height);

	String getClassName();

	void setClassName(String className);

	String getExClassName();

	void setExClassName(String exClassName);

	FormElementType getType();

	void setType(FormElementType type);

	String getTrigger();

	void setTrigger(String trigger);

	String getLabelSeparator();

	void setLabelSeparator(String labelSeparator);

	boolean isShowLabel();

	void setShowLabel(boolean showLabel);

	int getLabelWidth();

	void setLabelWidth(int labelWidth);

	int getLabelSpacing();

	void setLabelSpacing(int labelSpacing);

	FormElementLabelPosition getLabelPosition();

	void setLabelPosition(FormElementLabelPosition labelPosition);

	Align getLabelAlign();

	void setLabelAlign(Align labelAlign);

	int getEditorWidth();

	void setEditorWidth(int editorWidth);

	boolean isShowHint();

	void setShowHint(boolean showHint);

	int getHintWidth();

	void setHintWidth(int hintWidth);

	int getHintSpacing();

	void setHintSpacing(int hintSpacing);

	boolean isShowHintMessage();

	void setShowHintMessage(boolean showHintMessage);

	FormElementHintPosition getHintPosition();

	void setHintPosition(FormElementHintPosition hintPosition);

	boolean isReadOnly();

	void setReadOnly(boolean readOnly);
}
