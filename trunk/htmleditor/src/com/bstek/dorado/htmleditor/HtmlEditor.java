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
	private String mode = "full";
	private String defaultFontFamily = "宋体";
	private String defaultFontSize = "16px";
	private String fileUploadPath = ">dorado/htmleditor/fileupload";
	private String flashUploadPath = ">dorado/htmleditor/flashupload";
	private String imageUploadPath = ">dorado/htmleditor/imageupload";
	
	@ViewAttribute(defaultValue = "宋体")
	public String getDefaultFontFamily() {
		return defaultFontFamily;
	}

	public void setDefaultFontFamily(String defaultFontFamily) {
		this.defaultFontFamily = defaultFontFamily;
	}
	
	@ViewAttribute(defaultValue = "16px")
	public String getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(String defaultFontSize) {
		this.defaultFontSize = defaultFontSize;
	}

	@ViewAttribute(defaultValue = "full")
	public String getMode() {
		return mode;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	@ViewAttribute(defaultValue = ">dorado/htmleditor/fileupload")
	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	@ViewAttribute(defaultValue = ">dorado/htmleditor/flashupload")
	public String getFlashUploadPath() {
		return flashUploadPath;
	}

	public void setFlashUploadPath(String flashUploadPath) {
		this.flashUploadPath = flashUploadPath;
	}

	@ViewAttribute(defaultValue = ">dorado/htmleditor/imageupload")
	public String getImageUploadPath() {
		return imageUploadPath;
	}

	public void setImageUploadPath(String imageUploadPath) {
		this.imageUploadPath = imageUploadPath;
	}
	
}