package com.bstek.dorado.htmleditor;

import com.bstek.dorado.annotation.ViewAttribute;
import com.bstek.dorado.annotation.ViewObject;
import com.bstek.dorado.annotation.Widget;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.view.widget.form.AbstractDataEditor;

@Widget(name = "HtmlEditor", category = "Advance", dependsPackage = "html-editor")
@ViewObject(prototype = "dorado.widget.HtmlEditor", shortTypeName = "HtmlEditor")
@XmlNode(nodeName = "HtmlEditor")
public class HtmlEditor extends AbstractDataEditor {
	private HtmlEditorMode mode = HtmlEditorMode.full;
	
	@ViewAttribute(defaultValue = "full")
	public HtmlEditorMode getMode() {
		return mode;
	}
	
	public void setMode(HtmlEditorMode mode) {
		this.mode = mode;
	}
	
}
