package com.bstek.dorado.htmleditor;

import com.bstek.dorado.annotation.ClientObject;
import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.view.annotation.Widget;
import com.bstek.dorado.view.widget.form.AbstractDataEditor;

@Widget(name = "HtmlEditor", category = "Advance",
		dependsPackage = "html-editor")
@ClientObject(prototype = "dorado.widget.HtmlEditor",
		shortTypeName = "HtmlEditor")
public class HtmlEditor extends AbstractDataEditor {
	private String mode = "full";
	private String defaultFontFamily = "宋体";
	private String defaultFontSize = "16px";
	private String fileUploadPath = ">dorado/htmleditor/fileupload";
	private String flashUploadPath = ">dorado/htmleditor/flashupload";
	private String imageUploadPath = ">dorado/htmleditor/imageupload";

	@ClientProperty(escapeValue = "宋体")
	public String getDefaultFontFamily() {
		return defaultFontFamily;
	}

	public void setDefaultFontFamily(String defaultFontFamily) {
		this.defaultFontFamily = defaultFontFamily;
	}

	@ClientProperty(escapeValue = "16px")
	public String getDefaultFontSize() {
		return defaultFontSize;
	}

	public void setDefaultFontSize(String defaultFontSize) {
		this.defaultFontSize = defaultFontSize;
	}

	@ClientProperty(escapeValue = "full")
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	@ClientProperty(escapeValue = ">dorado/htmleditor/fileupload")
	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	@ClientProperty(escapeValue = ">dorado/htmleditor/flashupload")
	public String getFlashUploadPath() {
		return flashUploadPath;
	}

	public void setFlashUploadPath(String flashUploadPath) {
		this.flashUploadPath = flashUploadPath;
	}

	@ClientProperty(escapeValue = ">dorado/htmleditor/imageupload")
	public String getImageUploadPath() {
		return imageUploadPath;
	}

	public void setImageUploadPath(String imageUploadPath) {
		this.imageUploadPath = imageUploadPath;
	}

}